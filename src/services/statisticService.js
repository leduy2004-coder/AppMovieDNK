import * as callPath from '../utils/httpRequest';

export const getTicketByYear = async (year) => {
    try {
        const res = await callPath.get('/statistic/getTicketByYear', {
            params: { year },
        });

        return res;
    } catch (err) {
        return { errCode: err.response.status };
    }
};

export const getTopCustomersByYear = async (year) => {
    try {
        const res = await callPath.get('/statistic/getTopCustomersByYear', {
            params: { year },
        });
        return res;
    } catch (err) {
        return { errCode: err.response.status };
    }
};

export const getSumMovieByYear = async (year) => {
    try {
        const res = await callPath.get('/statistic/getSumMovieByYear', {
            params: { year },
        });
  
        return res;
    } catch (err) {
        return { errCode: err.response.status };
    }
};
export const getSumTurnoverByYear = async (year) => {
    try {
        const res = await callPath.get('/statistic/getSumTurnoverByYear', {
            params: { year },
        });
        return res;
    } catch (err) {
        return { errCode: err.response.status };
    }
};
export const getSumTicketByYear = async (year) => {
    try {
        const res = await callPath.get('/statistic/getSumTicketByYear', {
            params: { year },
        });
        return res;
    } catch (err) {
        return { errCode: err.response.status };
    }
};