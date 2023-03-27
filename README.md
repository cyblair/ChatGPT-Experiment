# ChatGPT-Experiment
 Python program for generating and saving large sets of prompt responses

Running this program requires you have access to the OpenAI API and have an API key. As per suggestion by the API,
os.getenv() is used to get an Environment variable storing the key.

Placing your api key in the OPENAI_API_KEY Environment variable will allow you to use this
software.

Note: Currently there isn't a lot of support for all the language models displayed you can
choose between. text-davinci-003 (and presumable the other text-davinci models) works fine. Other than that, nothing is confirmed to work

# Documentation
For the moment, everything should be pretty straight forward.

If you are saving a large number of java projects (as was needed for this experiment), you can prompt the AI to name the programs
'{title}'. {title} gets replaced by the name of the file when actually sending the prompt (file name being the 'Name of File' field
concatenated with the iteration number). This way, you don't have to rename the classes/.java files to compile.

{title} may be able to be used in other creative ways.