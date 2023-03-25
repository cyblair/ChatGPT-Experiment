import os
import openai

chatmodel = "text-davinci-002"

key = os.getenv("OPENAI_API_KEY")
openai.api_key = key

chatprompt = """Write a only java program called {title} that will use JOptionPanes to allow the user to 
    input text and an integer 'n', using a rotation cipher n times and returning the result.""";

def getCompletion(i):
    return openai.Completion.create(
        model=chatmodel,
        prompt=chatprompt.format(title = "Cipher"+str(i+1)),
        max_tokens=2048
    )

for i in range(0, 10):
    completion = getCompletion(i)
    print(completion.choices[0].text)