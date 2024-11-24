import React, { useState, useContext, useEffect } from 'react';
import PropTypes from 'prop-types';

const NotifyContext = React.createContext();

export function UserNotify() {
    return useContext(NotifyContext);
}

export function NotifyProvider({ children }) {
    const [infoNotify, setInfoNotify] = useState({
        content: '',
        delay: 3000,
        isNotify: false,
        type: 'success',
    });

    // Khi infoNotify.isNotify là true, sau một khoảng thời gian delay, cập nhật isNotify về false
    useEffect(() => {
        if (infoNotify.isNotify) {
            const timer = setTimeout(() => {
                setInfoNotify((prev) => ({
                    ...prev,
                    isNotify: false,
                }));
            }, infoNotify.delay*1.5);

            // Cleanup timer khi component bị unmount hoặc trước khi chạy effect mới
            return () => clearTimeout(timer);
        }
    }, [infoNotify]);

    const values = { infoNotify, setInfoNotify };

    return <NotifyContext.Provider value={values}>{children}</NotifyContext.Provider>;
}

NotifyProvider.propTypes = {
    children: PropTypes.node.isRequired,
};
