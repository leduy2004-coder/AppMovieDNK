import React from 'react';
import { Card, CardBody, CardTitle, ListGroup, CardSubtitle, ListGroupItem, Button } from 'reactstrap';

const FeedDataTemplate = [
    {
        titleKey: 'sumTicket', 
        title: 'Tổng số vé bán được',
        icon: 'bi bi-bell',
        color: 'primary',
    },
    {
        titleKey: 'sumTurnover', 
        title: 'Tổng doanh thu',
        icon: 'bi bi-person',
        color: 'info',
    },
    {
        titleKey: 'sumMovie',
        title: 'Tổng phim',
        icon: 'bi bi-hdd',
        color: 'danger',
    },
];


const General = ({ data }) => {
    console.log(data)
    // Kết hợp FeedDataTemplate với dữ liệu thực tế từ `data`
    const feedData = FeedDataTemplate.map((feed) => ({
        ...feed,
        value: data[feed.titleKey] || '0',
    }));

    return (
        <Card>
            <CardBody>
                <CardTitle tag="h5">Tổng quát</CardTitle>
                <ListGroup flush className="mt-4">
                    {feedData.map((feed, index) => (
                        <ListGroupItem key={index} className="d-flex align-items-center p-3 border-0">
                            <Button className="rounded-circle me-3" size="sm" color={feed.color}>
                                <i className={feed.icon}></i>
                            </Button>
                            <div>
                                <div>{feed.title}</div>
                                <div className="text-muted">{feed.value}</div>
                            </div>
                        </ListGroupItem>
                    ))}
                </ListGroup>
            </CardBody>
        </Card>
    );
};

export default General;
