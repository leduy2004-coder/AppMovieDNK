import { login, logout } from './authService';
import { getAllMovies, getAllTypeMovie, insertMovie, getMovieById, updateMovie, removeMovie } from './movieService';
import {getAllSchedule, getAllRoom, getAllShift ,insertSchedule, removeSchedule} from './scheduleService';
import { updateAbout } from './aboutService';

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
    getMovieById,
    updateMovie,
    removeMovie,
    getAllSchedule,
    getAllRoom, 
    getAllShift,
    insertSchedule,
    removeSchedule,
    updateAbout,
};

export default config;
