import { login, logout } from './authService';
import { getAllMovies, getAllTypeMovie, insertMovie, getMovieById } from './movieService';
import {
    getTicketByYear,
    getTopCustomersByYear,
    getSumMovieByYear,
    getSumTicketByYear,
    getSumTurnoverByYear,
} from './statisticService';

const config = {
    login,
    logout,
    getTicketByYear,
    getTopCustomersByYear,
    getSumMovieByYear,
    getSumTicketByYear,
    getSumTurnoverByYear,
    getAllMovies,
    getAllTypeMovie,
    insertMovie,
    getMovieById
};

export default config;
