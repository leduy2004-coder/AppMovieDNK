import { lazy } from 'react';
import { Navigate } from 'react-router-dom';

/****Layouts*****/
const FullLayout = lazy(() => import('../layouts/FullLayout.js'));

/***** Pages ****/
const Statistics = lazy(() => import('../views/Statistics.js'));
const About = lazy(() => import('../views/About.js'));
const Movie = lazy(() => import('../views/Movie.js'));
const Schedule = lazy(() => import('../views/Schedule.js'));
const Login = lazy(() => import('../views/Login.js'));
const DetailMovie = lazy(() => import('../views/DetailMovie'));

/*****Routes******/
const ThemeRoutes = [
    {
        path: '/login',
        element: <Login />,
    },

    {
        path: '/',
        element: <FullLayout />,
        children: [
            { path: '/', element: <Navigate to="/login" /> },
            { path: '/starter', element: <Statistics /> },
            { path: '/about', element: <About /> },
            { path: '/movie', element: <Movie /> },
            { path: '/movie/:id', element: <DetailMovie /> },
            { path: '/schedule', element: <Schedule /> },
        ],
    },
];

export default ThemeRoutes;
