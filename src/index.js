import React, { Suspense } from 'react';
import { createRoot } from 'react-dom/client';
import './assets/scss/style.scss';
import App from './App';
import reportWebVitals from './reportWebVitals';
import Loader from './layouts/loader/Loader';
import { BrowserRouter } from 'react-router-dom';
import { NotifyProvider } from './components/store/NotifyContext';
import { AuthProvider } from './components/store/AuthContext';
const rootElement = document.getElementById('root');
const root = createRoot(rootElement);

root.render(
    <NotifyProvider>
        <AuthProvider>
            <Suspense fallback={<Loader />}>
                <BrowserRouter>
                    <App />
                </BrowserRouter>
            </Suspense>
        </AuthProvider>
    </NotifyProvider>,
);

reportWebVitals();
