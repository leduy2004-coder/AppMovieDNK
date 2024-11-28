import React, { useState, useContext, useEffect } from 'react';
import PropTypes from 'prop-types';
const AuthContext = React.createContext();

export function UserAuth() {
    return useContext(AuthContext);
}

const isJSON = (str) => {
    try {
        JSON.parse(str);
    } catch (e) {
        return false;
    }
    return true;
};

export function AuthProvider({ children }) {
    const [userAuth, setUserAuth] = useState('');
    const [openFormAddMovie, setOpenFormAddMovie] = useState(false);
    const [openFormAddSchedule, setOpenFormAddSchedule] = useState(false);
    useEffect(() => {
        const storedUser = localStorage.getItem('userInfo');

        setUserAuth(storedUser && isJSON(storedUser) ? JSON.parse(storedUser) : '');
    }, []);

    const value = {
        userAuth,
        openFormAddMovie,
        setOpenFormAddMovie,
        openFormAddSchedule,
        setOpenFormAddSchedule,
    };

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

AuthProvider.propTypes = {
    children: PropTypes.node.isRequired,
};
