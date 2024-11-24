import React, { useEffect, useState } from 'react';
import { Alert } from 'reactstrap';
import { UserNotify } from './store/NotifyContext';

const Notify = () => {
    const { infoNotify } = UserNotify();

    const [isNotify, setIsNotify] = useState(infoNotify.isNotify);
    const [isEndAnimate, setIsEndAnimate] = useState(false);

    useEffect(() => {
        setIsNotify(infoNotify.isNotify);
        setIsEndAnimate(false);

        let timeoutEndAnimate = setTimeout(() => {
            setIsEndAnimate(true);
        }, infoNotify.delay);

        return () => {
            clearTimeout(timeoutEndAnimate);
        };
    }, [infoNotify]);

    return (
        <>
            {isNotify && (
                <div
                    className={`p-3 my-2 rounded position-fixed`} 
                    style={{ top: 20, right: 20, zIndex: 9999 }}
                >
                    <Alert 
                        color={infoNotify.type === 'success' ? 'success' : 'danger'}
                        isOpen={!isEndAnimate}
                        toggle={() => setIsNotify(false)}
                    >
                        {infoNotify.content || 'Có lỗi xảy ra!'}
                    </Alert>
                </div>
            )}
        </>
    );
};

export default Notify;
