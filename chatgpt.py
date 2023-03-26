import os
import tkinter as tk
from tkinter import filedialog
import openai

global WINDOW_WIDTH
global WINDOW_HEIGHT
global MAX_TOKENS
WINDOW_WIDTH = 640
WINDOW_HEIGHT = 360
MAX_TOKENS = 2048

def getModelIds():
    modelIdList = []
    modelList = openai.Model.list().data
    for model in modelList:
        modelIdList.append(model.root)
    return modelIdList

class ChatGPTCollector(tk.Frame):
    def __init__(self, master):
        frtop = tk.Frame(master)
        tk.Label(frtop, text="Folder to save projects to:").pack()

        self.folder = tk.StringVar()
        tk.Entry(frtop, textvariable=self.folder, width=50).pack()
        
        tk.Button(frtop, text="Browse", command=self.browseFiles).pack()
        frtop.pack()

        frleft = tk.Frame(master)

        tk.Label(frleft, text="Name of file:").pack()
        self.fileName = tk.StringVar()
        tk.Entry(frleft, textvariable=self.fileName, width=10).pack()

        tk.Label(frleft, text="File Extension:").pack()
        self.extension = tk.StringVar()
        tk.Entry(frleft, textvariable=self.extension, width=10).pack()

        tk.Label(frleft, text="Chat model to use:").pack()

        self.dropdown_modelType = tk.Listbox(frleft, selectmode=tk.SINGLE, height=6)
        chatModels = getModelIds()
        for model in chatModels:
            self.dropdown_modelType.insert(0, model)
        self.dropdown_modelType.pack()
        frleft.pack(side=tk.LEFT)

        frright = tk.Frame(master)
        tk.Label(frright, text="Prompt:").pack()
        self.prompt = tk.Text(frright, height=5, wrap=tk.WORD)
        self.prompt.pack()
        tk.Label(frright, text="Iterations:").pack()
        self.iterations = tk.StringVar()
        tk.Entry(frright, textvariable=self.iterations).pack()
        tk.Button(frright, text="Run", command=lambda:self.getCompletions(int(self.iterations.get()))).pack()
        frright.pack(side=tk.RIGHT)

        master.title("ChatGPT Data Collector")
        master.geometry(str(WINDOW_WIDTH)+"x"+str(WINDOW_HEIGHT))
        master.mainloop()

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
    
    def getCompletions(self, iterations):
        model = self.getSelectedChatModel()
        chatPrompt = self.prompt.get("1.0", tk.END)
        for i in range(iterations):
            name = self.fileName.get()+str(i)
            completion = openai.Completion.create(
                model=model,
                prompt=chatPrompt.format(title=name),
                max_tokens=MAX_TOKENS
            )
            f = open(self.folder.get()+"/"+name+self.extension.get(), "w")
            f.write(completion.choices[0].text)
            f.close()
        return

def main():
    key = os.getenv("OPENAI_API_KEY")
    openai.api_key = key

    ChatGPTCollector(tk.Tk())
    
if __name__ == "__main__":
    main()