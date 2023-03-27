import os
import tkinter as tk
from tkinter import ttk
from tkinter import filedialog
from tkinter import messagebox
import openai
import asyncio
import threading
import time
import json

WINDOW_WIDTH = 640
WINDOW_HEIGHT = 360
MAX_TOKENS = 2048
FILES = [("JSON files", "*.json"),
         ("All Files", "*.*")
        ]

def asyncioThread(async_loop, collector):
    async_loop.run_until_complete(collector.getCompletions(collector.getIterations(), collector.getSelectedChatModel()))

def getCompletions(async_loop, collector):
    threading.Thread(target=asyncioThread, args=(async_loop, collector)).start()

# Gets a list of language models available
def getModelIds():
    modelIdList = []
    modelList = openai.Model.list().data
    for model in modelList:
        modelIdList.append(model.root)
    return modelIdList

class ChatGPTCollector(tk.Frame):
    def __init__(self, master):
        self.async_loop = asyncio.get_event_loop()

        # Top Region
        frtop = ttk.Frame(master)

        # Save location input
        ttk.Label(frtop, text="Folder to save projects to:").pack()
        self.folder = tk.StringVar()
        ttk.Entry(frtop, textvariable=self.folder, width=50).pack()    
        ttk.Button(frtop, text="Browse", command=self.selectSaveFolder).pack()
        # Pack top Region
        frtop.pack()

        # Left Region
        frleft = ttk.Frame(master)

        # File name input
        ttk.Label(frleft, text="Name of file:").pack()
        self.fileName = tk.StringVar()
        ttk.Entry(frleft, textvariable=self.fileName, width=10).pack()

        # File extension input
        ttk.Label(frleft, text="File Extension:").pack()
        self.extension = tk.StringVar()
        ttk.Entry(frleft, textvariable=self.extension, width=10).pack()

        # Chat Model selection
        ttk.Label(frleft, text="Chat model to use:").pack()
        self.dropdown_modelType = tk.Listbox(frleft, selectmode=tk.SINGLE, height=6)
        chatModels = getModelIds()
        for model in chatModels:
            self.dropdown_modelType.insert(0, model)
        self.dropdown_modelType.pack()

        #Pack Left Region
        frleft.pack(side=tk.LEFT)

        # Right Region
        frright = ttk.Frame(master)

        # Prompt input
        ttk.Label(frright, text="Prompt:").pack()
        self.prompt = tk.Text(frright, height=7, wrap=tk.WORD, font=("Segoe UI", "8"))
        self.prompt.pack()
        # Iterations input
        ttk.Label(frright, text="Iterations:").pack()
        self.iterations = tk.StringVar()
        ttk.Entry(frright, textvariable=self.iterations).pack()
        # Get OpenAI Completions
        ttk.Button(frright, text="Run", command=lambda:self.startCompletionProcess(self.getIterations())).pack()

        ttk.Label(frright, text="Configurations:").pack(side=tk.LEFT, padx=20)
        ttk.Button(frright, text="Save", command=self.saveConfig).pack(side=tk.LEFT)
        ttk.Button(frright, text="Load", command=self.loadConfig).pack(side=tk.LEFT)

        # Pack Right Region
        frright.pack(side=tk.RIGHT)

        # Set up master window
        master.title("ChatGPT Data Collector")
        master.geometry(str(WINDOW_WIDTH)+"x"+str(WINDOW_HEIGHT))
        master.mainloop()

    def startCompletionProcess(self, iterations):
        # Make a new window to display progress
        self.win = tk.Tk()
        self.prgrsLbl = ttk.Label(self.win, text="Progress (0/"+str(iterations)+"): ")
        # For every iteration, we update value on getting completion and finishing writing to file
        maximum = iterations*2
        self.bar = ttk.Progressbar(self.win, maximum=maximum, value=0, length=250)
        self.etaLbl = ttk.Label(self.win, text="Estimated Time remaining: ")
        self.prgrsLbl.pack()
        self.bar.pack()
        self.etaLbl.pack()
        self.win.geometry("300x80")

        # Get our process start time
        self.start = time.time()
        # Start the process
        getCompletions(self.async_loop, self)
        # Call our progress window's main loop
        self.win.mainloop()

    """Save a .json file containing the data currently entered into fields"""
    def saveConfig(self):
        # Get file
        f = filedialog.asksaveasfile(confirmoverwrite=True, filetypes=FILES, defaultextension=FILES)
        # Create json string from object
        jstring = json.dumps({'folder':self.folder.get(), 'file':self.fileName.get(), 
                              'ext':self.extension.get(), 'model':self.dropdown_modelType.curselection(),
                              'prompt':self.prompt.get("1.0", tk.END)
                              })
        # Write json to file and close
        f.write(jstring)
        f.close()

    """Load a .json file and write it's data to this windows fields"""
    def loadConfig(self):
        # Get file
        f = filedialog.askopenfile(filetypes=FILES, defaultextension=FILES)
        
        # If no file was selected, return
        if (f == None):
            return
        
        # Try to read data and write to fields
        try:
            jstring = f.read()
            obj = json.loads(jstring)

            # Load up vars. If we don't do this, and a variable is missing, config will be partly loaded.
            # We just want to throw out malformatted json files without changing anything
            folder = obj['folder']
            file = obj['file']
            ext = obj['ext']
            model = obj['model']
            prompt = obj['prompt']

            self.folder.set(folder)
            self.fileName.set(file)
            self.extension.set(ext)
            if (len(model) > 0):
                self.dropdown_modelType.see(model[0])
                self.dropdown_modelType.index(model[0])
                self.dropdown_modelType.activate(model[0])
            # Clear current prompt and then insert prompt from file
            self.prompt.delete("1.0", tk.END)
            self.prompt.insert("1.0", prompt)
        # If something fails, i.e. malformatted file, wrong file type, etc., show an error
        except:
            messagebox.showerror(title="File Error", 
                                message="Something went wrong trying to load the specified file."+
                                " Please only select files saved with this program.")
        # Close file
        f.close()

    """Gets the int entered in the iterations field"""
    def getIterations(self):
        try:
            return int(self.iterations.get())
        # If the field is empty, default to 0
        except:
            return 0

    """Allows the user to set which folder to save outputs to"""
    def selectSaveFolder(self):
        path = filedialog.askdirectory()
        self.folder.set(path)

    """Gets the String name of the selected language model"""
    def getSelectedChatModel(self):
        menu = self.dropdown_modelType
        selection = menu.curselection()
        if (selection != ()):
            return str(menu.get(selection))
        else:
            return ""
    
    """Starts the process of submitting prompts and getting completions from OpenAI"""
    async def getCompletions(self, iterations, model):
        # Set up vars
        chatPrompt = self.prompt.get("1.0", tk.END)
        # Do process for each iteration
        for i in range(iterations):
            # Get the completion
            await self.getCompletion(model, chatPrompt, i)
            # Update progress
            self.prgrsLbl['text'] = "Progress ("+str(i+1)+"/"+str(iterations)+"): "
            # Estimate time remaining
            etaSeconds = round(((time.time()-self.start)/(i+1))*(iterations-i))
            etaMinutes, etaSeconds = divmod(etaSeconds, 60)
            # Update time estimation
            self.etaLbl['text'] = "Estimated time remaining: %dm %ds" % (etaMinutes, etaSeconds)
        
        # Once we are done getting completions, close the progress window
        self.win.destroy()
        # Show the user a message to indicate the process completed successfully
        messagebox.showinfo(title="Complete", message="Saved %d responses with the %s language model." % (iterations, model))
    
    """Make request to the respective OpenAI model and save completion"""
    async def getCompletion(self, model, chatPrompt, iteration):
        i = iteration
        name = self.fileName.get()+str(i)
        # TODO: Expand exception handling to modify request for different models
        try:
            # Get a completion
            completion = openai.Completion.create(
                model=model,
                prompt=chatPrompt.format(title=name),
                max_tokens=MAX_TOKENS
                )
            self.bar['value'] += 1
            # Write completion to file
            f = open(self.folder.get()+"/"+name+self.extension.get(), "w")
            f.write(completion.choices[0].text)
            f.close()
            self.bar['value'] += 1
        except openai.InvalidRequestError as e:
            print(e)

def main():
    key = os.getenv("OPENAI_API_KEY")
    openai.api_key = key

    ChatGPTCollector(tk.Tk())
    
if __name__ == "__main__":
    main()