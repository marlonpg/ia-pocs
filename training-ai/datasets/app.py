import datetime
import os
import pandas as pd
from surprise import Dataset, Reader, SVD
from surprise.model_selection import cross_validate
import pickle
from flask import Flask, jsonify, request

movies_path = os.path.join('movies_metadata_v2.csv')
ratings_path = os.path.join('ratings.csv')
model_path = 'trained_model.pkl'

def load_dataset_for_training():
    print("Start loading datasets for training.")
    movies = pd.read_csv(movies_path, low_memory=False)
    ratings = pd.read_csv(ratings_path)
    ratings['movieId'] = ratings['movieId'].astype(int)
    print("Finished loading datasets for training.")
    return movies, ratings

# Check if the model file exists (already trained):
if os.path.exists(model_path):
    with open(model_path, 'rb') as model_file:
        model = pickle.load(model_file)

    movies = pd.read_csv(movies_path, low_memory=False)
    movies = movies.dropna()
    print("Model loaded from file.")
else:
    movies, ratings = load_dataset_for_training()
    reader = Reader(rating_scale=(0.5, 5.0))
    data = Dataset.load_from_df(ratings[['userId', 'movieId', 'rating']], reader)

    print("Started the training of the model datetime:", datetime.datetime.now())
    model = SVD()
    cross_validate(model, data, measures=['RMSE', 'MAE'], cv=5, verbose=True)
    trainset = data.build_full_trainset()
    print("Finished the training of the model datetime:", datetime.datetime.now())
    model.fit(trainset)

    with open('trained_model.pkl', 'wb') as model_file:
        pickle.dump(model, model_file)

def get_movie_metadata(movie_id):
    movie = movies[movies['id'] == str(movie_id)].iloc[0]
    return f"Title: {movie['title']}, Genres: {movie['genres']}, link: {movie['poster_path']}"

# API:

def get_default_recommendations(n_recommendations=5):
    default_movies = movies.sample(n=n_recommendations)
    return [get_movie_metadata(movie_id) for movie_id in default_movies['id']]

app = Flask(__name__)

@app.route('/recommend_movies', methods=['POST'])
def recommend_movies():
    data = request.get_json()
    user_id = data.get('user_id')
    n_recommendations=5

    if user_id not in model.trainset.all_users():
        return jsonify(get_default_recommendations())

    predictions = [model.predict(user_id, int(movie_id)) for movie_id in movies['id'].astype(int)]
    predictions.sort(key=lambda x: x.est, reverse=True)
    top_n = predictions[:n_recommendations]
    recommendations = [get_movie_metadata(pred.iid) for pred in top_n]
    return jsonify(recommendations)


if __name__ == '__main__':
    app.run(debug=True)

# user_id = 1
# recommendations = recommend_movies(user_id, model)
# for rec in recommendations:
#     print(rec)
