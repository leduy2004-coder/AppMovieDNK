import React, { useState } from 'react';
import {
    Row,
    Col,
    Card,
    CardBody,
    CardTitle,
    CardSubtitle,
    CardText,
    Button,
    Input,
    InputGroup,
    InputGroupText,
    Pagination,
    PaginationItem,
    PaginationLink,
} from 'reactstrap';
import { FaPlus, FaTrashAlt, FaInfoCircle } from 'react-icons/fa';
import DetailMovie from '~/components/DetailMovie';


// Dữ liệu mẫu
const movieData = [
    { id: 1, image: 'image1.jpg', title: 'Movie 1', duration: '120 mins' },
    { id: 2, image: 'image2.jpg', title: 'Movie 2', duration: '95 mins' },
    { id: 3, image: 'image3.jpg', title: 'Movie 3', duration: '110 mins' },
    { id: 4, image: 'image4.jpg', title: 'Movie 4', duration: '140 mins' },
    { id: 5, image: 'image5.jpg', title: 'Movie 5', duration: '100 mins' },
    // Thêm dữ liệu phim khác...
];

const Movie = () => {
    const [movies, setMovies] = useState(movieData);
    const [search, setSearch] = useState('');

    // Hàm xử lý tìm kiếm
    const handleSearch = (e) => {
        setSearch(e.target.value);
    };

    // Lọc phim theo từ khóa tìm kiếm
    const filteredMovies = movies.filter((movie) =>
        movie.title.toLowerCase().includes(search.toLowerCase())
    );
    
    return (
        <div>
            {/* Thanh tìm kiếm và nút thêm */}
            <Row className="mb-4 align-items-center">
                <Col md="9">
                    <InputGroup>
                        <Input
                            placeholder="Tìm kiếm theo tên phim..."
                            value={search}
                            onChange={handleSearch}
                        />
                        <InputGroupText>
                            <i className="bi bi-search"></i>
                        </InputGroupText>
                    </InputGroup>
                </Col>
                <Col md="3" className="text-end">
                    <Button color="primary">
                        <FaPlus className="me-2" />
                        Thêm phim
                    </Button>
                </Col>
            </Row>

            {/* Danh sách phim */}
            <Row>
                {filteredMovies.map((movie) => (
                    <Col sm="6" lg="4" xl="3" key={movie.id} className="mb-4">
                        <Card>
                            <img src={movie.image} alt={movie.title} className="card-img-top" />
                            <CardBody>
                                <CardTitle tag="h5">{movie.title}</CardTitle>
                                <CardSubtitle tag="h6" className="mb-2 text-muted">
                                    {movie.duration}
                                </CardSubtitle>
                                <div className="d-flex justify-content-between">
                                    <Button color="danger" size="sm">
                                        <FaTrashAlt className="me-1" />
                                        Xóa
                                    </Button>
                                    <Button color="info" size="sm">
                                        <FaInfoCircle className="me-1" />
                                        Xem chi tiết
                                    </Button>
                                </div>
                            </CardBody>
                        </Card>
                    </Col>
                ))}
            </Row>

            {/* Phân trang */}
            <Row>
                <Col>
                    <Pagination className="justify-content-center">
                        <PaginationItem disabled>
                            <PaginationLink previous />
                        </PaginationItem>
                        <PaginationItem active>
                            <PaginationLink>1</PaginationLink>
                        </PaginationItem>
                        <PaginationItem>
                            <PaginationLink>2</PaginationLink>
                        </PaginationItem>
                        <PaginationItem>
                            <PaginationLink>3</PaginationLink>
                        </PaginationItem>
                        <PaginationItem>
                            <PaginationLink next />
                        </PaginationItem>
                    </Pagination>
                </Col>
            </Row>
        </div>
    );
};


export default Movie;
