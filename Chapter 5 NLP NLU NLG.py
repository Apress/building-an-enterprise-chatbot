#Natural Language Processing

#Process Textual Data

#Read the CSV file

import pandas as pd
food_review = pd.read_csv("Reviews.csv")
food_review.head()

#Sampling

food_review_text = pd.DataFrame(food_review["Text"])
food_review_text_1k = food_review_text.sample(n= 1000,random_state = 123) 
food_review_text_1k.head()

#Tokenization using NLTK

food_review_text_1k['tokenized_reviews'] = food_review_text_1k['Text'].apply(nltk.word_tokenize)
food_review_text_1k.head()

#Word Search using Regex

#Search: All 5-letter word with c as its first letter and i as its third letter
search_word = set([w for w in food_review_text_1k['tokenized_reviews'].iloc[0] if re.search('^c.i..$', w)])
print(search_word) 

#Word search using the exact word

#Search for the word 'great' in reviews
food_review_text_1k[food_review_text_1k['Text'].str.contains('great')]

#NLTK
#Normalization using NLTK

words = set(food_review_text_1k['tokenized_reviews'].iloc[0])
print(words)

porter = nltk.PorterStemmer()
print([porter.stem(w) for w in words])

lancaster = nltk.LancasterStemmer()
print([lancaster.stem(w) for w in words])

#Noun Phrase Chunking using Regular expression

import nltk
from nltk.tokenize import word_tokenize

#Noun Phrase Chunking
text = word_tokenize("My English Bulldog Larry had skin allergies the summer we got him at age 3, I'm so glad that now I can buy his food from Amazon")

#This grammar rule: Find NP chunk when an optional determiner (DT) is followed by any number of adjectives (JJ) and then a noun (NN)

grammar = "NP: {<DT>?<JJ>*<NN>}"

#Regular Expression Parser using the above grammar
cp = nltk.RegexpParser(grammar)

#Parsed text with pos tag
review_chunking_out = cp.parse(nltk.pos_tag(text))

#Print the parsed text
print(review_chunking_out)

from nltk.chunk import conlltags2tree, tree2conlltags
from pprint import pprint

#Print IOB tags
review_chunking_out_IOB = tree2conlltags(review_chunking_out)
pprint(review_chunking_out_IOB)

#Named Entity Recognition

tagged_review_sent = nltk.pos_tag(text)
print(nltk.ne_chunk(tagged_review_sent))

#spaCy

#POS Tagging

# POS Tagging
import spacy

nlp = spacy.load('en_core_web_sm')
doc = nlp(u"My English Bulldog Larry had skin allergies the summer we got him at age 3, I'm so glad that now I can buy his food from Amazon")

for token in doc:
    print(token.text, token.lemma_, token.pos_, token.tag_, token.dep_,
            token.shape_, token.is_alpha, token.is_stop)

#Dependency Parse

import spacy

nlp = spacy.load("en_core_web_sm")
doc = nlp(u"My English Bulldog Larry had skin allergies the summer we got him at age 3, I'm so glad that now I can buy his food from Amazon")
for chunk in doc.noun_chunks:
    print(chunk.text, chunk.root.text, chunk.root.dep_,
            chunk.root.head.text)

#Dependency Tree

import spacy
from spacy import displacy

nlp = spacy.load("en_core_web_sm")
doc = nlp(u"My English Bulldog Larry had skin allergies the summer we got him at age 3")

displacy.render(doc, style='dep')

#Chunking

# pip install spacy
# python -m spacy download en_core_web_sm

import spacy

# Load English tokenizer, tagger, parser, NER and word vectors
nlp = spacy.load("en_core_web_sm")

# Process whole documents
text = ("My English Bulldog Larry had skin allergies the summer we got him at age 3, I'm so glad that now I can buy his food from Amazon")
doc = nlp(text)

# Analyze syntax
print("Noun phrases:", [chunk.text for chunk in doc.noun_chunks])
print("Verbs:", [token.lemma_ for token in doc if token.pos_ == "VERB"])

#Named Entity Recognition

import spacy

# Load English tokenizer, tagger, parser, NER and word vectors
nlp = spacy.load("en_core_web_sm")

# Process whole documents
text = ("My English Bulldog Larry had skin allergies the summer we got him at age 3, I'm so glad that now I can buy his food from Amazon")
doc = nlp(text)

# Find named entities
for entity in doc.ents:
    print(entity.text, entity.label_)

import spacy
from spacy import displacy
from pathlib import Path

text = "I found these crisps at our local WalMart & figured I would give them a try. They were so yummy I may never go back to regular chips, not that I was a big chip fan anyway. The only problem is I can eat the entire bag in one sitting. I give these crisps a big thumbs up!"

nlp = spacy.load("en_core_web_sm")
doc = nlp(text)
svg = displacy.serve(doc, style="ent")
output_path = Path("images/sentence_ne.svg")
output_path.open("w", encoding="utf-8").write(svg)

#Pattern-based search
# Spacy - Rule-based matching

import spacy
from spacy.matcher import Matcher

nlp = spacy.load("en_core_web_sm")
matcher = Matcher(nlp.vocab)
#Search for Walmart after converting the text in lower case and 
pattern = [{"LOWER": "walmart"}, {"IS_PUNCT": True}]
matcher.add("Walmart", None, pattern)

doc = nlp(u"I found these crisps at our local WalMart & figured I would give them a try. They were so yummy I may never go back to regular chips, not that I was a big chip fan anyway. The only problem is I can eat the entire bag in one sitting. I give these crisps a big thumbs up!")

matches = matcher(doc)

for match_id, start, end in matches:
    string_id = nlp.vocab.strings[match_id]  # Get string representation
    span = doc[start:end]  # The matched span
    print(match_id, string_id, start, end, span.text)

#Search for entity

from spacy.lang.en import English
from spacy.pipeline import EntityRuler

nlp = English()
ruler = EntityRuler(nlp)
patterns = [{"label": "ORG","pattern":[{"lower":"walmart"}]}]

ruler.add_patterns(patterns)
nlp.add_pipe(ruler)

doc = nlp(u"I found these crisps at our local WalMart & figured I would give them a try. They were so yummy I may never go back to regular chips, not that I was a big chip fan anyway. The only problem is I can eat the entire bag in one sitting. I give these crisps a big thumbs up!")
print([(ent.text, ent.label_) for ent in doc.ents])

#Training a custom NLP model

import spacy
import random

train_data = [
        (u"As soon as I tasted one and it tasted like a corn chip I checked the ingredients. ", {"entities": [(45, 49, "PRODUCT")]}),
        (u"I found these crisps at our local WalMart & figured I would give them a try", {"entities": [(14, 20, "PRODUCT")]})
]

other_pipes = [pipe for pipe in nlp.pipe_names if pipe != "ner"]

with nlp.disable_pipes(*other_pipes):
    optimizer = nlp.begin_training()
    for i in range(10):
        random.shuffle(train_data)
        for text, annotations in train_data:
            nlp.update([text], [annotations], sgd=optimizer)
nlp.to_disk("model/food_model")

#Prediction
import spacy
nlp = spacy.load("model/food_model")
text = nlp("I consume about a jar every two weeks of this, either adding it to fajitas or using it as a corn chip dip")

for entity in text.ents:
    print(entity.text, entity.label_)

#CoreNLP

#Tokenize

from stanfordcorenlp import StanfordCoreNLP

nlp = StanfordCoreNLP(<Path to CoreNLP folder>)

sentence = 'I consume about a jar every two weeks of this, either adding it to fajitas or using it as a corn chip dip'

print('Tokenize:', nlp.word_tokenize(sentence))

#Part-of-speech tagging
print('Part of Speech:', nlp.pos_tag(sentence))

#Named Entity Recognition

print('Named Entities:', nlp.ner(sentence))

#Constituency Parsing

print('Constituency Parsing:', nlp.parse(sentence))

#Dependency Parsing
print('Dependency Parsing:', nlp.dependency_parse(sentence))

nlp.close()

#TextBlob

#POS tags and noun phrase

#First, the import
from textblob import TextBlob

#create our first TextBlob
s_text = TextBlob("Building Enterprise Chatbot that can converse like humans")

#Part-of-speech tags can be accessed through the tags property.
s_text.tags

#Similarly, noun phrases are accessed through the noun_phrases property
s_text.noun_phrases

#Spelling Correction

# Spelling Correction
# Use the correct() method to attempt spelling correction.
# Spelling correction is based on Peter Norvig’s “How to Write a Spelling Corrector” as implemented in the pattern library. It is about 70% accurate

b = TextBlob("Building Enterprise Chatbot that can converce like humans. The future for chatbot looks great!")
print(b.correct())

#Machine Translation

#Translation and Language Detection
# Google Translate API powers language translation and detection.

en_blob = TextBlob(u'Building Enterprise Chatbot that can converse like humans. The future for chatbot looks great!')
en_blob.translate(to='fr')


#Multilingual Text Processing

#Textblob for translation

from textblob import TextBlob

#A News brief from the French news website: https://www.lemonde.fr/

fr_blob = TextBlob(u"Des nouveaux matchs de Paire et Mahut au retour du service à la cuillère, tout ce qu’il ne faut pas rater à Roland-Garros, sur les courts ou en dehors, ce vendredi.")
fr_blob.translate(to='en')

#POS and Dependency relation

import spacy

#Download: python -m spacy download fr_core_news_sm
nlp = spacy.load('fr_core_news_sm')
french_text = nlp("Des nouveaux matchs de Paire et Mahut au retour du service à la cuillère, tout ce qu’il ne faut pas rater à Roland-Garros, sur les courts ou en dehors, ce vendredi.")

for token in french_text:
    print(token.text, token.pos_, token.dep_)

#Named Entity Recognition

# Find named entities, phrases, and concepts
for entity in french_text.ents:
    print(entity.text, entity.label_)

#Noun Phrases
for fr_chunk in french_text.noun_chunks:
    print(fr_chunk.text, fr_chunk.root.text, fr_chunk.root.dep_,
            fr_chunk.root.head.text)


#Natural Language Understanding

#Sentiment Analysis

s_text = TextBlob("Building Enterprise Chatbot that can converse like humans. The future for chatbot looks great!")
s_text.sentiment

#Language Models

#Word2Vec
#n-grams
#The TextBlob.ngrams() method returns a list of tuples of n successive words.
#First, the import
from textblob import TextBlob

blob = TextBlob("Building an enterprise chatbot that can converse like humans")
blob.ngrams(n=2)

#Using word2vec pre-trained model

#Step 1: Load the required libraries

from gensim.test.utils import get_tmpfile
from gensim.models import Word2Vec
import gensim.models

#Step 2: Lets pick some words from the Amazon Food Review and make a list

review_texts = [['chips', 'WalMart', 'fajitas'],
 ['ingredients', 'tasted', 'crisps', 'Chilling', 'fridge', 'nachos'],
 ['tastebuds', 'tortilla', 'Mexican', 'baking'],
 ['toppings', 'goodness', 'product, 'fantastic']]

#Step 3: Train the Word2Vec model and save

path = get_tmpfile("word2vec.model")

model = Word2Vec(review_texts, size=100, window=5, min_count=1, workers=4)
model.save("word2vec.model")

from gensim.models.word2vec import FAST_VERSION
FAST_VERSION

#Step 4: Load the model and get the output word vector

model = Word2Vec.load("word2vec.model")

vector = model.wv['tortilla']

type(vector)

len(vector)

#Step 5: The word2vec model we saved in the step 3, could be loaded again and we can continue the training on more words using the train function in word2vec model.

more_review_texts = [['deceptive', 'packaging', 'wrappers'],
 ['texture', 'crispy', 'thick', 'cruncy', 'fantastic', 'rice']]

model = Word2Vec.load("word2vec.model")
model.train(more_review_texts, total_examples=2,epochs=10)

#Performing out-of-the-box tasks using a pre-trained model

#Step 1: Download one of the pre-trained GloVe word vectors using gensim.downloder module
import gensim.downloader as api
word_vectors = api.load("glove-wiki-gigaword-100")

#Step 2: Computing nearest neighbors

result = word_vectors.most_similar('apple')
print(result)

result = word_vectors.most_similar('orange')
print(result)

#Step 3: Identifying linear substructures
sim = word_vectors.n_similarity(['sushi', 'shop'], ['indian', 'restaurant'])
print("{:.4f}".format(sim))

sim = word_vectors.n_similarity(['sushi', 'shop'], ['japanese', 'restaurant'])
print("{:.4f}".format(sim))

#Sentence Similarity: 
sentence_one = 'India is a diverse country with many culinary art'.lower().split()
sentence_two = 'Delhi offers many authentic food'.lower().split()

similarity = word_vectors.wmdistance(sentence_one, sentence_two)
print("{:.4f}".format(similarity))

sentence_one = 'India is a diverse country with many culinary art'.lower().split()
sentence_two = 'The all-new Apple TV app, which brings together all the ways to watch TV into one app'.lower().split()

similarity = word_vectors.wmdistance(sentence_one, sentence_two)
print("{:.4f}".format(similarity))

#Arithmetic Operations: 
result = word_vectors.most_similar(positive=['woman', 'king'], negative=['man'])
print("{}: {:.4f}".format(*result[0]))

result = word_vectors.most_similar(positive=['french', 'italian'])
print("{}: {:.4f}".format(*result[0]))

result = word_vectors.most_similar(positive=['france', 'italy'])
print("{}: {:.4f}".format(*result[0]))

#Odd word out:

print(word_vectors.doesnt_match("india spain italy pizza".split()))

print(word_vectors.doesnt_match("obama trump bush modi".split()))

#fastText word representation model 

import fasttext

# Skipgram model :
model_sgram = fasttext.train_unsupervised('dataset/amzn_food_review_small.txt', model='skipgram')

# or, cbow model :
model_cbow = fasttext.train_unsupervised('dataset/amzn_food_review_small.txt', model='cbow')

print(model_sgram['cakes'])

print(model_sgram.words)

#Topic Modelling using Latent Dirichlet Allocation

#Collection of documents

documents = ["I consume about a jar every two weeks of this, either adding it to fajitas or using it as a corn chip dip,"
             "As soon as I tasted one and it tasted like a corn chip I checked the ingredients",
             "I found these crisps at our local WalMart & figured I would give them a try"    
]

#Load libraries and define stopwords

# import pretty printer
from pprint import pprint  
from collections import defaultdict
stoplist = set('for a of the and to in'.split())

#Remove common words and tokenize
# remove common words and tokenize
texts = [
     [word for word in document.lower().split() if word not in stoplist]
     for document in documents
 ]

#Remove words that appear infrequently

# remove words that appear only once
frequency = defaultdict(int)
for text in texts:
    for the token in text:
        frequency[token] += 1

texts = [
     [token for token in text if frequency[token] > 1]
     for text in texts
]

pprint(texts)

#Save the training data as dictionary
from gensim import corpora
dictionary = corpora.Dictionary(texts)
dictionary.save('review.dict')
print(dictionary)

print(dictionary.token2id)

new_doc = "tasty corn"
new_vec = dictionary.doc2bow(new_doc.lower().split())
print(new_vec)

#Generate the bag-of-word

corpus = [dictionary.doc2bow(text) for text in texts]
corpora.MmCorpus.serialize('review.mm', corpus)

#Train the model using LDA

from gensim import models
tfidf = models.TfidfModel(corpus)
corpus_tfidf = tfidf[corpus]
lsi = models.LsiModel(corpus_tfidf, id2word=dictionary, num_topics=2)
corpus_lsi = lsi[corpus_tfidf]
lsi.print_topics(2)

#Natural Language Generation

#Markov Chain based Headlines Generator

#Loading Required Packages
import pandas as pd # data processing, CSV file I/O (e.g. pd.read_csv)
import markovify #Markov Chain Generator

#Reading Input Text File

Input_text = pd.read_csv('data/abcnews-date-text.csv')
Input_text.head(3)

#Build Text Model using Makovify
text_model = markovify.NewlineText(input_text.headline_text, state_size = 2)

#Generate Random Headlines
# Print ten randomly-generated sentences using the built model
for i in range(10):
    print(text_model.make_sentence())

	
#SimpleNLG
#Load the library
import logging
from nlglib.realisation.simplenlg.realisation import Realiser
from nlglib.microplanning import *
realise = Realiser(host='nlg.kutlak.info')

#Tense
def tense():
    c = Clause('Harry', 'bought', 'these off amazon')
    c['TENSE'] = 'PAST'
    print(realise(c))
    c['TENSE'] = 'FUTURE'
    print(realise(c))

	
#Negation
def negation():
    c = Clause('Harry', 'bought', 'these off amazon')
    c['NEGATED'] = 'true'
    print(realise(c))

	
#Interrogative

def interrogative():
    c = Clause('Harry', 'bought', 'these off amazon')
    c['INTERROGATIVE_TYPE'] = 'YES_NO'
    print(realise(c))
    c['INTERROGATIVE_TYPE'] = 'WHO_OBJECT'
    print(realise(c))

#Complements
def complements():
    c = Clause('Harry', 'bought', 'these off amazon',
               complements=['on first day of sales', 'despite high price'])
    print(realise(c))

#Modifiers
def modifiers():
    subject = NP('Harry')
    verb = VP('bought')
    objekt = NP('these', 'off','amazon')
    subject += Adjective('Impulsive')
    c = Clause()
    c.subject = subject
    c.predicate = verb
    c.object = objekt
    print(realise(c))
    verb += Adverb('quickly')
    c = Clause(subject, verb, objekt)
    print(realise(c))

#Prepositional Phrase

def prepositional_phrase():
    c = Clause('Harry', 'bought', 'these off amazon')
    c.complements += PP('by', 'surprise')
    print(realise(c))
    c = Clause('Harry', 'bought', 'these off amazon')
    c.complements += PP('for', NP('Eva'))
    print(realise(c))

#Coordinated Clause

def coordinated_clause():
    s1 = Clause('Harry', 'buy', 'these off amazon', features={'TENSE': 'PAST'})
    s2 = Clause('he', 'like','jeans', features={'TENSE': 'PRESENT'})
    s3 = Clause('he', 'return', 't-shirt', features={'TENSE': 'FUTURE'})
    c = s1 + s2 + s3
    c = CC(s1, s2, s3)
    print(realise(s1))
    print(realise(s2))
    print(realise(s3))
    print(realise(s1 + s2))
    print(realise(c))

#Subordinate Clause
def subordinate_clause():
    p = Clause('Harry', 'like', 'amazon')
    q = Clause('product', 'is', 'good')
    q['COMPLEMENTISER'] = 'because'
    q['TENSE'] = 'PAST'
    p.complements += q
    print(realise(p))

#Main method
def main():
    c = Clause('Harry', 'bought', 'these off amazon')
    print(realise(c))
    tense()
    negation()
    interrogative()
    complements()
    modifiers()
    prepositional_phrase()
    coordinated_clause()
    subordinate_clause()

#Printing the output
if __name__ == '__main__':
    logging.basicConfig(level=logging.WARNING)
    main()

#Deep Learning Model for Text Generation

#Loading the library

from keras.preprocessing.sequence import pad_sequences
from keras.layers import Embedding, LSTM, Dense, Dropout
from keras.preprocessing.text import Tokenizer
from keras.callbacks import EarlyStopping
from keras.models import Sequential
import keras.utils as ku 
import numpy as np

#Defining the training data

review_data = ""Chilling in the fridge seems to boost the flavor even more; 
and using them, rather than corn chips, to make nachos will have your tastebuds 
singing like Janet Jackson but without any of the associated wardrobe risks."

#Data Preparation

#Tokenization to extract terms or words from a corpus
review_tokenizer = Tokenizer()
def dataset_preparation(review_data):
    corpus = review_data.lower().split("\n")    
    review_tokenizer.fit_on_texts(corpus)
    total_words = len(review_tokenizer.word_index) + 1
    
    #convert the corpus into a flat dataset
    input_review_sequences = []
    for line in corpus:
        token_list = review_tokenizer.texts_to_sequences([line])[0]
        for i in range(1, len(token_list)):
            n_gram_sequence = token_list[:i+1]
            input_review_sequences.append(n_gram_sequence)
            
    #pad the sequences
    max_sequence_len = max([len(x) for x in input_review_sequences])
    input_review_sequences = np.array(pad_sequences(input_review_sequences,   
                          maxlen=max_sequence_len, padding='pre'))
    
    #predictor and label data
    predictors, label = input_review_sequences[:,:-1],input_review_sequences[:,-1]
    label = ku.to_categorical(label, num_classes=total_words)
    
    return predictors, label, max_sequence_len, total_words

#Create RNN Architecture using LSTM network

#RNN Model

def create_model(predictors, label, max_sequence_len, total_words):
    input_len = max_sequence_len - 1
    model = Sequential()
    model.add(Embedding(input_dim = total_words, output_dim = 10, input_length=input_len))
    model.add(LSTM(150))
    model.add(Dropout(0.1))
    model.add(Dense(total_words, activation='softmax'))
    model.compile(loss='categorical_crossentropy', optimizer='adam')
    model.fit(predictors, label, epochs=100, verbose=1)
    return model

#Define generate text method

def generate_text(seed_text, next_words, max_sequence_len, model):
    for j in range(next_words):
        token_list = review_tokenizer.texts_to_sequences([seed_text])[0]
        token_list = pad_sequences([token_list], maxlen= 
                             max_sequence_len-1, padding='pre')
        predicted = model.predict_classes(token_list, verbose=0)
  
        output_word = ""
        for word, index in review_tokenizer.word_index.items():
            if index == predicted:
                output_word = word
                break
        seed_text += " " + output_word
    return seed_text

#Train the RNN model

X, Y, max_len, total_words = dataset_preparation(review_data)
model = create_model(X, Y, max_len, total_words)

#Generate Text

text = generate_text("singing like", 3, max_len, model)
print(text)

#Applications

#Topic Modelling using all spaCy, NLTK and gensim library

#Tokenize and Cleaning the Text
Code Snippet
# Clean
import spacy
spacy.load('en_core_web_md')
from spacy.lang.en import English
parser = English()

def tokenize_review_text(text):
    lda_review_tokens = []
    review_tokens = parser(text)
    for token in review_tokens:
        if token.orth_.isspace():
            continue
        elif token.like_url:
            lda_review_tokens.append('URL')
        elif token.orth_.startswith('@'):
            lda_review_tokens.append('SCREEN_NAME')
        else:
            lda_review_tokens.append(token.lower_)
    return lda_review_tokens

#Lemmatization

import nltk
nltk.download('wordnet')
from nltk.corpus import wordnet as wordNet

def get_lemma(word):
    lemma = wordNet.morphy(word)
    if lemma is None:
        return word
    else:
        return lemma
    
#Preprocess text method for LDA

from nltk.stem.wordnet import WordNetLemmatizer
def get_lemma2(word):
    return WordNetLemmatizer().lemmatize(word)

# Remove english stopwork
nltk.download('stopwords')
en_stop = set(nltk.corpus.stopwords.words('english'))

def preprocess_text_for_lda(input_review_text):
    tokens = tokenize_review_text(input_review_text)
    tokens = [token for token in tokens if len(token) > 4]
    tokens = [token for token in tokens if token not in en_stop]
    tokens = [get_lemma(token) for token in tokens]
    return tokens

preprocess_text_for_lda("I consume about a jar every two weeks of this, either adding it to fajitas or using it as a corn chip dip")

['consume', 'every', 'week', 'either', 'add', 'fajitas', 'using']

#Read the training data 

review_text_data = []
with open('data/corn_review.txt') as f:
    for line in f:
        tokens = preprocess_text_for_lda(line)
        print(tokens)
        review_text_data.append(tokens)

#Bag of words

#LDA gensim

from gensim import corpora
corn_review_dict = corpora.Dictionary(review_text_data)
corn_review_corpus = [corn_review_dict.doc2bow(text) for text in review_text_data]

import pickle
pickle.dump(corpus, open('corn_review_corpus.pkl', 'wb'))
dictionary.save('corn_review_dict.gensim')

#Train and save the model

import gensim
number_of_topics = 5
corn_review_ldamodel = gensim.models.ldamodel.LdaModel(corn_review_corpus, num_topics = number_of_topics, id2word=corn_review_dict, passes=15)
corn_review_ldamodel.save('corn_review_ldamodel.gensim')
topics = corn_review_ldamodel.print_topics(num_words=4)
for topic in topics:
    print(topic)

#Predictions
new_doc = 'Corn is typically yellow but comes in a variety of other colors, such as red, orange, purple, blue, white, and black.'
new_doc = preprocess_text_for_lda(new_doc)
new_doc_bow = corn_review_dict.doc2bow(new_doc)
print(new_doc_bow)
print(corn_review_ldamodel.get_document_topics(new_doc_bow))

new_doc = 'corn tortilla or just tortilla is a type of thin, unleavened flatbread'
new_doc = preprocess_text_for_lda(new_doc)
new_doc_bow = corn_review_dict.doc2bow(new_doc)
print(new_doc_bow)
print(corn_review_ldamodel.get_document_topics(new_doc_bow))

#Gender Identification
#Load the NLTK library and download names corpus
import nltk
nltk.download('names')

#Load the male and female names

names = nltk.corpus.names
names.fileids()

male_names = names.words('male.txt')
female_names = names.words('female.txt')

#Common names
print([w for w in male_names if w in female_names])

#Extract features
def gender_features(word):
    return {'last_letter': word[-1]}
gender_features('Shrek')

#Randomly split into Train and Test

from nltk.corpus import names
labeled_names = ([(name, 'male') for name in names.words('male.txt')] + [(name, 'female') for name in names.words('female.txt')])

import random
random.shuffle(labeled_names)

featuresets = [(gender_features(n), gender) for (n, gender) in labeled_names]
train_set, test_set = featuresets[500:], featuresets[:500]

#Train the model
classifier = nltk.NaiveBayesClassifier.train(train_set)

#Model Prediction
classifier.classify(gender_features('John'))

classifier.classify(gender_features('Sascha'))

#Model Accuracy

print(nltk.classify.accuracy(classifier, test_set))

#Most Informative Features

classifier.show_most_informative_features(5)

#Document Classification

#Load Libraries

import os
import random
from nltk.corpus.reader.plaintext import CategorizedPlaintextCorpusReader

#Read the dataset into the categorized corpus

# Directory of the corpus
corpusdir = 'corpus/' 
review_corpus = CategorizedPlaintextCorpusReader(corpusdir, r'.*\.txt', cat_pattern=r'\d+_(\w+)\.txt')

# list of documents(fileid) and category (pos/neg)
documents = [(list(review_corpus.words(fileid)), category)
              for category in review_corpus.categories()
              for fileid in review_corpus.fileids(category)]
random.shuffle(documents)

for category in review_corpus.categories():
    print(category)

type(review_corpus)

len(documents)

#Compute word frequency

import nltk
all_words = nltk.FreqDist(w.lower() for w in review_corpus.words())
word_features = list(all_words)[:200]

print(word_features)

#Check the presence of frequent words

#Check whether most frequent word is present in the doc or not
def document_features(document):
    document_words = set(document)
    features = {}
    for word in word_features:
        features['contains({})'.format(word)] = (word in document_words)
    return features

print(document_features(review_corpus.words('1_pos.txt')))

print(document_features(review_corpus.words('1_neg.txt')))

#Train the model

featuresets = [(document_features(d), c) for (d,c) in documents]
train_set, test_set = featuresets[5:], featuresets[:5]
classifier = nltk.NaiveBayesClassifier.train(train_set)

print(nltk.classify.accuracy(classifier, test_set)) 

print(nltk.classify.accuracy(classifier, train_set)) 

#Most Informative Features
classifier.show_most_informative_features(5)

#Intent Classification and Question Answering

#Intent Classification

#Set the tensorflow as backend
import os
os.environ["KERAS_BACKEND"] = "tensorflow"

#Build the model

from deeppavlov import build_model, configs
CONFIG_PATH = configs.classifiers.intents_snips  # could also be configuration dictionary or string path or `pathlib.Path` instance
#model = build_model(CONFIG_PATH, download=True)  # run it once
model = build_model(CONFIG_PATH, download=False)  # otherwise

#Classify Intent

print(model(["will it rain in Edgbaston, Birmingham today?"]))
print(model(["book one table at a good restaurant?"]))
print(model(["Give Da Vinci Code a 5 star on my amazon purchase"]))
print(model(["what are the show times for The Lion King"]))

#Question Answering

#Build the model
from deeppavlov import build_model, configs


#model = build_model(configs.squad.squad, download=True)
model = build_model(configs.squad.squad)

#Context and Question

model(['IRIS is an enterprise chatbot completely built in-house and uses private data'], ['What is IRIS?'])

model(['Great morning cake!,We must have made about 20 of these cakes last fall They are so good. Also very easy to make.  This was great with bacon and eggs in the morning. It was also great for dessert (as I believe it was intended ;). We didnt put the icing on as suggested as the cake was great without it.  Now that its getting a little chilly out we are excited to start making our favorite fall cake again'], ['how many cakes were made?'])

model(['I used these rainbow jimmies for a rainbow cupcake topper and added them to rice krispie treats for my daughters 6th birthday.  Obviously, it was a rainbow party.  The package didnt look like the picture, but I was not disappointed in the product.  I would buy from this company again.'], ['is the customer happy about the purchase?'])

