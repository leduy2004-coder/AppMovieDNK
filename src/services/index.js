import { login, logout } from './authService';
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
};

export default config;
