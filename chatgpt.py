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
FILES = [("All Files", "*.*"),
        ("JSON files", "*.json")]

def asyncioThread(async_loop, collector):
    async_loop.run_until_complete(collector.getCompletions(collector.getIterations()))

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
        async_loop = asyncio.get_event_loop()

        # Top Region
        frtop = ttk.Frame(master)

        # Save location input
        ttk.Label(frtop, text="Folder to save projects to:").pack()
        self.folder = tk.StringVar()
        ttk.Entry(frtop, textvariable=self.folder, width=50).pack()    
        ttk.Button(frtop, text="Browse", command=self.browseFiles).pack()
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
        ttk.Button(frright, text="Run", command=lambda:getCompletions(async_loop, self)).pack()

        ttk.Label(frright, text="Configurations:").pack(side=tk.LEFT, padx=20)
        ttk.Button(frright, text="Save", command=self.saveConfig).pack(side=tk.LEFT)
        ttk.Button(frright, text="Load", command=self.loadConfig).pack(side=tk.LEFT)

        # Pack Right Region
        frright.pack(side=tk.RIGHT)

        master.title("ChatGPT Data Collector")
        master.geometry(str(WINDOW_WIDTH)+"x"+str(WINDOW_HEIGHT))
        master.mainloop()

    def saveConfig(self):
        f = filedialog.asksaveasfile(confirmoverwrite=True, filetypes=FILES, defaultextension=FILES)
        jstring = json.dumps({'folder':self.folder.get(), 'file':self.fileName.get(), 
                              'ext':self.extension.get(), 'model':self.dropdown_modelType.curselection(),
                              'prompt':self.prompt.get("1.0", tk.END)})
        f.write(jstring)
        f.close()

    def loadConfig(self):
        f = filedialog.askopenfile(filetypes=FILES, defaultextension=FILES)
        if (f == None):
            return
        
        try:
            jstring = f.read()
            obj = json.loads(jstring)

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
            self.prompt.delete("1.0", tk.END)
            self.prompt.insert("1.0", prompt)
        except:
            messagebox.showerror(title="File Error", 
                                message="Something went wrong trying to load the specified file."+
                                " Please only select files saved with this program.")
        f.close()

    def getIterations(self):
        try:
            return int(self.iterations.get())
        except:
            return 0

    def browseFiles(self):
        path = filedialog.askdirectory()
        self.folder.set(path)

    def getSelectedChatModel(self):
        menu = self.dropdown_modelType
        selection = menu.curselection()
        if (selection != ()):
            return str(menu.get(selection))
        else:
            return ""
    
    async def getCompletions(self, iterations):
        win = tk.Tk()
        prgrsLbl = ttk.Label(win, text="Progress (0/"+str(iterations)+"): ")
        bar = ttk.Progressbar(win, maximum=iterations, value=0, length=250)
        etaLbl = ttk.Label(win, text="Estimated Time remaining: ")
        prgrsLbl.pack()
        bar.pack()
        etaLbl.pack()

        win.geometry("300x80")
        win.update()

        start = time.time()
        model = self.getSelectedChatModel()
        chatPrompt = self.prompt.get("1.0", tk.END)
        for i in range(iterations):
            await self.getCompletion(model, chatPrompt, i)
            prgrsLbl['text'] = "Progress ("+str(i+1)+"/"+str(iterations)+"): "
            bar['value'] = i+1
            etaSeconds = round(((time.time()-start)/(i+1))*(iterations-i))
            etaMinutes, etaSeconds = divmod(etaSeconds, 60)
            etaLbl['text'] = "Estimated time remaining: %dm %ds" % (etaMinutes, etaSeconds)
            win.update()

        win.destroy()
    
    async def getCompletion(self, model, chatPrompt, iteration):
        i = iteration
        name = self.fileName.get()+str(i)
        try:
            completion = openai.Completion.create(
                model=model,
                prompt=chatPrompt.format(title=name),
                max_tokens=MAX_TOKENS
            )
            f = open(self.folder.get()+"/"+name+self.extension.get(), "w")
            f.write(completion.choices[0].text)
            f.close()
        except openai.InvalidRequestError as e:
            print(e)

def main():
    key = os.getenv("OPENAI_API_KEY")
    openai.api_key = key

    ChatGPTCollector(tk.Tk())
    
if __name__ == "__main__":
    main()