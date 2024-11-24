import React from 'react';
import { Routes, Route } from 'react-router-dom';
import ThemeRoutes from './routes/Router';
import FullLayout from './layouts/FullLayout';
import Notify from './components/Notify';
const App = () => {
    return (
        <Routes>
            {ThemeRoutes.map((route, index) => {
                return route.children ? (
                    <Route key={index} path={route.path} element={<FullLayout />}>
                        {route.children.map((childRoute, childIndex) => (
                            <Route
                                key={childIndex}
                                path={childRoute.path}
                                element={childRoute.element}
                            />
                        ))}
                    </Route>
                ) : (
                    <Route key={index} path={route.path} element={route.element} />
                );
            })}
        </Routes>
    );
};

export default App;
