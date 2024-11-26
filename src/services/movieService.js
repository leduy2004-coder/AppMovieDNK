import * as callPath from '../utils/httpRequest';

export const getAllMovies = async () => {
    try {
        const res = await callPath.get('/movies/getAll', {});

        return res;
    } catch (err) {
        return { errCode: err.response.status };
    }
};
export const getMovieById = async (maPhim) => {
    try {
        const res = await callPath.get(`/movies/phim/${maPhim}`, {});

        return res;
    } catch (err) {
        return { errCode: err.response.status };
    }
};
export const getAllTypeMovie = async () => {
    try {
        const res = await callPath.get('/movies/getTypeMovie', {});

        return res;
    } catch (err) {
        return { errCode: err.response.status };
    }
};

export const insertMovie = async (movieDetails) => {
    try {
        const res = await callPath.post('/movies/insert', movieDetails, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });
        // console.log(res.data)
        return res.data;
    } catch (err) {
        return { errCode: err.response.status };
    }
};

export const updateMovie = async (idMovie, formData) => {
    try {
        console.log(idMovie);
        const res = await callPath.patch(`/movies/update-movie/${idMovie}`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });
        return res.data;
    } catch (err) {
        return { errCode: err.response.status };
    }
};

export const removeMovie = async (idMovie) => {
    try {
        const res = await callPath.deleted(`/movies/delete/${idMovie}`, {});

        return res.data;
    } catch (err) {
        return { errorCode: err.response.status };
    }
};
