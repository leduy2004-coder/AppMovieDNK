import React, { useState, useRef, useEffect } from 'react';
import { Row, Col, Card, CardTitle, Form, FormGroup, Label, Input, Button } from 'reactstrap';
import { UserAuth } from '~/components/store/AuthContext';
import { UserNotify } from '~/components/store/NotifyContext';
import './form.scss';
import config from '~/services';


const UpdateScheduleForm = ({ schedule }) => {
    const { setInfoNotify } = UserNotify();
    const { setOpenFormAddSchedule } = UserAuth();
    console.log(schedule);
    const [scheduleDetails, setScheduleDetails] = useState({
        maPhim: '',
        maPhong: '',
        maCa: '',
        ngayChieu: '',
        tinhTrang: ''
    });
    const [listMovieTitle, setListMovieTitle] = useState([]);
    const [listRoom, setListRoom] = useState([]);
    const [listShow, setListShow] = useState([]);

    useEffect(() => {
       
    
            const mappedData = {
                maPhim: schedule.maPhim || '',
                maPhong: schedule.maPhong || '',
                maCa: schedule.maCa || '',
                ngayChieu: schedule.ngayChieu && !isNaN(Date.parse(schedule.ngayChieu)) 
    ? new Date(schedule.ngayChieu).toISOString().split('T')[0] 
    : '',
                tinhTrang: schedule.tinhTrang || '',
            };
            console.log(mappedData.releaseDate)
            setScheduleDetails(mappedData);

        
        // lấy tất cả tên phim
        const fetchMovieTitle = async () => {
            try {
                const data = await config.getAllMovies();
                console.log("API Response:", data);  // Log toàn bộ data nhận được
                if (data.errCode) {
                    setInfoNotify({
                        content: 'Lỗi dữ liệu !!',
                        delay: 1500,
                        isNotify: true,
                        type: 'error',
                    });
                } else {
                    setListMovieTitle(data); // Set data for combobox
                }
            } catch (error) {
                console.log(error)
                setInfoNotify({
                    content: 'Lỗi khi lấy dữ liệu từ server !! getAllMovies',
                    delay: 1500,
                    isNotify: true,
                    type: 'error',
                });
            }
        };

        // lấy tất cả ca chiếu còn trống trong ngày
        const fetchRoom = async () => {
            try {
                const data = await config.getAllRoom();
                console.log("API Response:", data);  // Log toàn bộ data nhận được
                if (data.errCode) {
                    setInfoNotify({
                        content: 'Lỗi dữ liệu !!',
                        delay: 1500,
                        isNotify: true,
                        type: 'error',
                    });
                } else {
                    setListRoom(data); // Thiết lập dữ liệu cho combobox
                }
            } catch (error) {
                console.log(error)
                setInfoNotify({
                    content: 'Lỗi khi lấy dữ liệu từ server !! getAllRoom',
                    delay: 1500,
                    isNotify: true,
                    type: 'error',
                });
            }
        };
       

        
        fetchMovieTitle();
        fetchRoom();

    }, [setListRoom,setListShow]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setScheduleDetails((prev) => ({ ...prev, [name]: value }));
    };

   

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
           
            let data;
            let actionMessage = '';

            if (schedule.tenPhim) {
                 
                 data = await config.updateMovie(scheduleDetails.maSuat, scheduleDetails);
                actionMessage = 'Cập nhật phim thành công !!';
            } else {
                console.log(scheduleDetails)
                data = await config.insertSchedule(scheduleDetails);
                actionMessage = 'Thêm phim thành công !!';
            }
            if (data.errCode) {
                setInfoNotify({
                    content: 'Lỗi dữ liệu !!',
                    delay: 1500,
                    isNotify: true,
                    type: 'error',
                });
            } else {
                setOpenFormAddSchedule(false);
                setInfoNotify({
                    content: 'Thêm phim thành công !!',
                    delay: 1500,
                    isNotify: true,
                    type: 'success',
                });
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

    const handleClose = () => {
        setOpenFormAddSchedule(false); // Close the form
    };

useEffect(() => {
    const fetchShow = async (ngayChieu, maPhong) => {
        try {
            const data = await config.getAvailableShifts(ngayChieu, maPhong);
            console.log(data)
            if (data.errCode) {
                setInfoNotify({
                    content: 'Lỗi dữ liệu !!',
                    delay: 1500,
                    isNotify: true,
                    type: 'error',
                });
            } else {
                setListShow(data); // Set data for combobox
            }
        } catch (error) {
            console.log(error)
            setInfoNotify({
                content: 'Lỗi khi lấy dữ liệu từ server !! getAllMovies',
                delay: 1500,
                isNotify: true,
                type: 'error',
            });
        }
    };
    if (scheduleDetails.maPhong && scheduleDetails.ngayChieu) {
        fetchShow(scheduleDetails.ngayChieu, scheduleDetails.maPhong);
    }
}, [scheduleDetails.maPhong, scheduleDetails.ngayChieu]); 


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
                                {schedule.tenPhim ? 'Cập nhật lịch chiếu' : 'Thêm mới lịch chiếu'}
                            </CardTitle>
                            <Form onSubmit={handleSubmit}>
                                <Row>
                                    {/* Cột 1 */}
                                    <Col md={12} >
                                        {/* Movie Title */}
                                        <FormGroup>
                                            <Label for="maPhim">Tên phim</Label>
                                            <Input
                                                type="select"
                                                id="maPhim"
                                                name="maPhim"
                                                value={scheduleDetails.maPhim}
                                                onChange={handleChange}
                                                required
                                            >
                                                <option value="">-- Chọn loại phim --</option>
                                                {Array.isArray(listMovieTitle) && listMovieTitle.length > 0 ? (
                                                    listMovieTitle.map((type) => (
                                                        <option key={type.maPhim} value={type.maPhim}>
                                                            {type.tenPhim}
                                                        </option>
                                                    ))
                                                ) : (
                                                    <option disabled>No movies available</option>
                                                )}
                                            </Input>
                                        </FormGroup>

                                        {/* Release Date */}
                                        <FormGroup>
                                            <Label for="ngayChieu">Ngày phát hành</Label>
                                            <Input
                                                type="date"
                                                id="ngayChieu"
                                                name="ngayChieu"
                                                value={scheduleDetails.ngayChieu}
                                                onChange={handleChange}
                                                required
                                            />
                                        </FormGroup>

                                        {/* Director */}
                                        <FormGroup>
                                        <Label for="maPhong">Phòng chiếu</Label>
                                            <Input
                                                type="select"
                                                id="maPhong"
                                                name="maPhong"
                                                value={scheduleDetails.maPhong}
                                                onChange={handleChange}
                                                required
                                            >
                                                <option value="">-- Chọn phòng chiếu --</option>
                                                {Array.isArray(listRoom) && listRoom.length > 0 ? (
                                                    listRoom.map((type) => (
                                                        <option key={type.maPhong} value={type.maPhong}>
                                                            {type.maPhong}
                                                        </option>
                                                    ))
                                                ) : (
                                                    <option disabled>No movies available</option>
                                                )}
                                            </Input>
                                           
                                        </FormGroup>

                                        {/* Age Requirement */}
                                        <FormGroup>
                                            <Label for="maCa">Ca chiếu</Label>
                                            <Input
                                                type="select"
                                                id="maCa"
                                                name="maCa"
                                                value={scheduleDetails.maCa}
                                                onChange={handleChange}
                                                required
                                            >
                                                <option value="">-- Chọn ca chiếu --</option>
                                                {Array.isArray(listShow) && listShow.length > 0 ? (
                                                    listShow.map((type) => (
                                                        <option key={type.maCa} value={type.maCa}>
                                                            {type.tenCa}
                                                        </option>
                                                    ))
                                                ) : (
                                                    <option disabled>No movies available</option>
                                                )}
                                            </Input>
                                        </FormGroup>

                                        <FormGroup>
                                            <Label for="tinhTrang">Tình trạng</Label>
                                            <Input
                                                type="select"
                                                id="tinhTrang"
                                                name="tinhTrang"
                                                value={scheduleDetails.tinhTrang}
                                                onChange={handleChange}
                                                required
                                            >
                                                <option value="">-- Chọn tình trạng --</option>
                                                <option value="1">1 - Available</option>
                                                <option value="0">0 - Sold out</option>
                                            </Input>
                                        </FormGroup>
                                    </Col>
                                </Row>

                            

                                <div className="area-btn-submit">
                                    <Button className="btn-submit" type="submit">
                                    {schedule.tenPhim ? 'Cập nhật lịch chiếu' : 'Thêm mới lịch chiếu'}
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

export default UpdateScheduleForm;
