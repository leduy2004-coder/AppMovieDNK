import React, { useState, useRef, useEffect } from 'react';
import { Row, Col, Card, CardTitle, Form, FormGroup, Label, Input, Button } from 'reactstrap';
import { UserAuth } from '~/components/store/AuthContext';
import { UserNotify } from '~/components/store/NotifyContext';
import './form.scss';
import config from '~/services';

const UpdateMovieForm = ({ movieId, movies, onMoviesUpdate }) => {
    const { setInfoNotify } = UserNotify();
    const { setOpenFormAddMovie } = UserAuth();
    const [selectedFile, setSelectedFile] = useState(false);
   
    const [movieDetails, setMovieDetails] = useState({
        movieTitle: '',
        releaseDate: '',
        director: '',
        ageRequirement: '',
        duration: '',
        movieType: '',
        movieImage: null,
        movieVideo: '',
        description: '',
    });

    const [typeMovie, setTypeMovie] = useState([]);

    useEffect(() => {
        if (!movieId) {
            return;
        }
        const fetchData = async () => {
            try {
                console.log(movieId);

                const data = await config.getMovieById(movieId);
                console.log(data);
                if (data.errCode) {
                    setInfoNotify({
                        content: 'Lỗi dữ liệu !!',
                        delay: 1500,
                        isNotify: true,
                        type: 'error',
                    });
                } else {
                    const mappedData = {
                        movieTitle: data.tenPhim || '',
                        releaseDate: data.ngayKhoiChieu ? new Date(data.ngayKhoiChieu).toISOString().split('T')[0] : '',
                        director: data.daoDien || '',
                        ageRequirement: data.doTuoiYeuCau || '',
                        duration: data.thoiLuong || '',
                        movieType: data.maLPhim || '',
                        movieImage: data.hinhDaiDien || null,
                        movieVideo: data.video || '',
                        description: data.moTa || '',
                    };
                    setSelectedFile(true);

                    console.log(mappedData.releaseDate);
                    setMovieDetails(mappedData);
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
    }, [movieId]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const data = await config.getAllTypeMovie();

                if (data.errCode) {
                    setInfoNotify({
                        content: 'Lỗi dữ liệu !!',
                        delay: 1500,
                        isNotify: true,
                        type: 'error',
                    });
                } else {
                    setTypeMovie(data); // Set data for combobox
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

    const handleChange = (e) => {
        const { name, value } = e.target;
        setMovieDetails((prev) => ({ ...prev, [name]: value }));
    };

    const handleFileChange = (e) => {
        setSelectedFile(false);

        const { name, files } = e.target;
        setMovieDetails((prev) => ({ ...prev, [name]: files[0] }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            let data;
            let actionMessage = '';

            if (movieId) {
                data = await config.updateMovie(movieId, movieDetails);
                actionMessage = 'Cập nhật phim thành công !!';
            } else {
                data = await config.insertMovie(movieDetails);
                actionMessage = 'Thêm phim thành công !!';
            }

            if (data.errCode) {
                setInfoNotify({
                    content: 'Lỗi dữ liệu !!',
                    delay: 1500,
                    isNotify: true,
                    type: 'error',
                });
                return;
            }

            // Cập nhật danh sách movies
            const updatedMovies = movieId
                ? movies.map((movie) => (movie.maPhim === movieId ? { ...movie, ...data } : movie))
                : [data, ...(movies || [])];
            console.log(updatedMovies);
            console.log(data);
            onMoviesUpdate(updatedMovies); // Cập nhật state ở cha
            setOpenFormAddMovie(false); // Đóng form

            setInfoNotify({
                content: actionMessage,
                delay: 1500,
                isNotify: true,
                type: 'success',
            });
        } catch (error) {
            setInfoNotify({
                content: 'Lỗi khi lấy dữ liệu từ server !!',
                delay: 1500,
                isNotify: true,
                type: 'error',
            });
        }
    };

    const handleClose = () => {
        setOpenFormAddMovie(false);
    };

    return (
        <div className="background-form">
            <Row>
                <Col>
                    <Card className="form-card">
                        <div className="form-content">
                            <div className="area-close">
                                <span className="close-btn" onClick={handleClose} role="button" aria-label="Close">
                                    &times;
                                </span>
                            </div>
                            <CardTitle tag="h4" className="text-center mb-4">
                                {movieId ? 'Cập nhật phim' : 'Thêm mới phim'}
                            </CardTitle>
                            <Form onSubmit={handleSubmit}>
                                <Row>
                                    {/* Cột 1 */}
                                    <Col md={6}>
                                        {/* Movie Title */}
                                        <FormGroup>
                                            <Label for="movieTitle">Tên phim</Label>
                                            <Input
                                                type="text"
                                                id="movieTitle"
                                                name="movieTitle"
                                                value={movieDetails.movieTitle}
                                                onChange={handleChange}
                                                placeholder="Nhập tên phim"
                                                required
                                            />
                                        </FormGroup>

                                        {/* Release Date */}
                                        <FormGroup>
                                            <Label for="releaseDate">Ngày phát hành</Label>
                                            <Input
                                                type="date"
                                                id="releaseDate"
                                                name="releaseDate"
                                                value={movieDetails.releaseDate}
                                                onChange={handleChange}
                                                required
                                            />
                                        </FormGroup>

                                        {/* Director */}
                                        <FormGroup>
                                            <Label for="director">Đạo diễn</Label>
                                            <Input
                                                type="text"
                                                id="director"
                                                name="director"
                                                value={movieDetails.director}
                                                onChange={handleChange}
                                                placeholder="Nhập tên đạo diễn"
                                                required
                                            />
                                        </FormGroup>

                                        {/* Age Requirement */}
                                        <FormGroup>
                                            <Label for="ageRequirement">Độ tuổi yêu cầu</Label>
                                            <Input
                                                type="number"
                                                id="ageRequirement"
                                                name="ageRequirement"
                                                value={movieDetails.ageRequirement}
                                                onChange={handleChange}
                                                placeholder="Nhập độ tuổi yêu cầu"
                                                required
                                            />
                                        </FormGroup>
                                    </Col>

                                    {/* Cột 2 */}
                                    <Col md={6}>
                                        {/* Duration */}
                                        <FormGroup>
                                            <Label for="duration">Thời lượng (phút)</Label>
                                            <Input
                                                type="number"
                                                id="duration"
                                                name="duration"
                                                value={movieDetails.duration}
                                                onChange={handleChange}
                                                placeholder="Nhập thời lượng phim"
                                                required
                                            />
                                        </FormGroup>

                                        {/* Movie Type */}
                                        <FormGroup>
                                            <Label for="movieType">Loại phim</Label>
                                            <Input
                                                type="select"
                                                id="movieType"
                                                name="movieType"
                                                value={movieDetails.movieType}
                                                onChange={handleChange}
                                                required
                                            >
                                                <option value="">-- Chọn loại phim --</option>
                                                {typeMovie.map((type) => (
                                                    <option key={type.maLPhim} value={type.maLPhim}>
                                                        {type.tenLPhim}
                                                    </option>
                                                ))}
                                            </Input>
                                        </FormGroup>

                                        {/* Movie Image */}
                                        <FormGroup>
                                            <Label for="movieImage">Ảnh bìa phim</Label>
                                            <div className="file-upload">
                                                {/* Display the image if available */}
                                                {movieDetails.movieImage && selectedFile && (
                                                    <img
                                                        src={movieDetails.movieImage}
                                                        alt="Movie"
                                                        style={{
                                                            width: '100px',
                                                            height: 'auto',
                                                            marginRight: '15px',
                                                            marginBottom: '10px',
                                                        }}
                                                    />
                                                )}
                                                <Input
                                                    type="file"
                                                    id="movieImage"
                                                    name="movieImage"
                                                    onChange={handleFileChange}
                                                    accept="image/*"
                                                    required={!movieDetails.movieImage}
                                                />
                                            </div>
                                        </FormGroup>

                                        {/* Movie Video */}
                                        <FormGroup>
                                            <Label for="movieVideo">Video phim</Label>
                                            <Input
                                                type="text"
                                                placeholder="Lấy url từ youtube"
                                                id="movieVideo"
                                                name="movieVideo"
                                                value={movieDetails.movieVideo}
                                                onChange={handleChange}
                                                accept="video/*"
                                            />
                                        </FormGroup>
                                    </Col>
                                </Row>

                                {/* Description */}
                                <FormGroup>
                                    <Label for="description">Mô tả</Label>
                                    <Input
                                        type="textarea"
                                        id="description"
                                        name="description"
                                        value={movieDetails.description}
                                        onChange={handleChange}
                                        placeholder="Nhập mô tả phim"
                                    />
                                </FormGroup>

                                <div className="area-btn-submit">
                                    <Button className="btn-submit" type="submit">
                                        {movieId ? 'Cập nhật phim' : 'Thêm phim'}
                                    </Button>
                                </div>
                            </Form>
                        </div>
                    </Card>
                </Col>
            </Row>
        </div>
    );
};

export default UpdateMovieForm;
