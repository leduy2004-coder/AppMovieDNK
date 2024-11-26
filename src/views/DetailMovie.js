import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

const DetailMovie = () => {
    const { id } = useParams();

    const [movieDetails, setMovieDetails] = useState(null);


    useEffect(() => {
        const fetchMovieDetails = async () => {
            try {
                const response = await fetch(`/api/movies/${id}`);
                const data = await response.json();
                setMovieDetails(data);
            } catch (error) {
                console.error("Error fetching movie details:", error);
            }
        };

        fetchMovieDetails();
    }, [id]);

    if (!movieDetails) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            <h2>Movie Detail: {movieDetails.title}</h2>
            <p>Genre: {movieDetails.genre}</p>
            <p>Release Date: {movieDetails.releaseDate}</p>
            <p>Description: {movieDetails.description}</p>
        </div>
    );
};

export default DetailMovie;
