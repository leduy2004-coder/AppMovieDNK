import React, { useState, useEffect } from 'react';
import {
    Row,
    Col,
    Card,
    CardBody,
    CardTitle,
    CardSubtitle,
    Button,
    Input,
    InputGroup,
    InputGroupText,
    Pagination,
    PaginationItem,
    PaginationLink,
} from 'reactstrap';
import { FaPlus, FaTrashAlt, FaInfoCircle } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';
import config from '~/services';
import { UserNotify } from '~/components/store/NotifyContext';
import { UserAuth } from '~/components/store/AuthContext';
import AddMovieForm from '~/components/form/FormUpdateMovie';
const Movie = () => {
    const [movies, setMovies] = useState([]);
    const [search, setSearch] = useState('');
    const [movieId, setMovieId] = useState('');
    const [currentPage, setCurrentPage] = useState(1);
    const { setInfoNotify } = UserNotify();
    const { openFormAddMovie, setOpenFormAddMovie } = UserAuth();
    const itemsPerPage = 12;
    const navigate = useNavigate();

    //Gọi api lấy list phim
    useEffect(() => {
        const fetchData = async () => {
            try {
                const data = await config.getAllMovies();

                if (data.errCode) {
                    setInfoNotify({
                        content: 'Lỗi dữ liệu !!',
                        delay: 1500,
                        isNotify: true,
                        type: 'error',
                    });
                } else {
                    setMovies(data);
                }
            } catch (error) {
                setInfoNotify({
                    content: 'Lỗi khi lấy dữ liệu từ server !!',
                    delay: 1500,
                    isNotify: true,
                    type: 'error',
                });
            }
        };

        fetchData();
    }, []);

    // Hàm xử lý tìm kiếm
    const handleSearch = (e) => {
        setSearch(e.target.value);
        setCurrentPage(1); // Reset về trang 1 khi tìm kiếm
    };

    // Lọc phim theo từ khóa tìm kiếm
    const filteredMovies = movies.filter((movie) => movie.tenPhim.toLowerCase().includes(search.toLowerCase()));

    // Phân trang
    const totalPages = Math.ceil(filteredMovies.length / itemsPerPage);
    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentMovies = filteredMovies.slice(indexOfFirstItem, indexOfLastItem);

    const handlePageChange = (pageNumber) => {
        setCurrentPage(pageNumber);
    };

    const handleViewDetails = (id) => {
        setMovieId(id);
        setOpenFormAddMovie(true);
    };

    const handOpenFormAdd = () => {
        setOpenFormAddMovie(true);
    };

    return (
        <div>
            {/* Thanh tìm kiếm và nút thêm */}
            <Row className="mb-4 align-items-center">
                <Col md="9">
                    <InputGroup>
                        <Input placeholder="Tìm kiếm theo tên phim..." value={search} onChange={handleSearch} />
                        <InputGroupText>
                            <i className="bi bi-search"></i>
                        </InputGroupText>
                    </InputGroup>
                </Col>
                <Col md="3" className="text-end">
                    <Button color="primary" onClick={handOpenFormAdd}>
                        <FaPlus className="me-2" />
                        Thêm phim
                    </Button>
                </Col>
            </Row>

            {/* Danh sách phim */}
            <Row>
                {currentMovies.map((movie) => (
                    <Col sm="6" lg="4" xl="3" key={movie.maPhim} className="mb-4">
                        <Card>
                            <div
                                className="card-img-container"
                                style={{
                                    width: '100%',
                                    height: '200px',
                                    overflow: 'hidden',
                                    borderRadius: '8px',
                                    position: 'relative',
                                }}
                            >
                                <img
                                    src={movie.hinhDaiDien}
                                    alt={movie.tenPhim}
                                    style={{
                                        width: '100%',
                                        height: '100%',
                                    }}
                                />
                            </div>

                            <CardBody>
                                <CardTitle tag="h5">{movie.tenPhim}</CardTitle>
                                <CardSubtitle tag="h6" className="mb-2 text-muted">
                                    {movie.thoiLuong} phút
                                </CardSubtitle>
                                <div className="d-flex justify-content-between">
                                    <Button color="danger" size="sm">
                                        <FaTrashAlt className="me-1" />
                                        Xóa
                                    </Button>
                                    <Button color="info" size="sm" onClick={() => handleViewDetails(movie.maPhim)}>
                                        <FaInfoCircle className="me-1" />
                                        Chỉnh sửa
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
                        <PaginationItem disabled={currentPage === 1}>
                            <PaginationLink previous onClick={() => handlePageChange(currentPage - 1)} />
                        </PaginationItem>
                        {Array.from({ length: totalPages }, (_, index) => (
                            <PaginationItem key={index + 1} active={currentPage === index + 1}>
                                <PaginationLink onClick={() => handlePageChange(index + 1)}>{index + 1}</PaginationLink>
                            </PaginationItem>
                        ))}
                        <PaginationItem disabled={currentPage === totalPages}>
                            <PaginationLink next onClick={() => handlePageChange(currentPage + 1)} />
                        </PaginationItem>
                    </Pagination>
                </Col>
            </Row>

            {openFormAddMovie && <AddMovieForm movieId={movieId} />}
        </div>
    );
};

export default Movie;
