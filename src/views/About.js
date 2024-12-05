import React, { useState, useEffect } from 'react';
import { Row, Col, Card, CardBody, CardTitle, Button, Input } from 'reactstrap';
import { FaSave, FaEdit } from 'react-icons/fa';
import user1 from '../assets/images/users/user4.jpg';
import { UserNotify } from '~/components/store/NotifyContext';
import config from '~/services';
const About = () => {
    const { setInfoNotify } = UserNotify();
    const [userInfo, setUserInfo] = useState({});
    const [editData, setEditData] = useState({
        hoTen: '',
        sdt: '',
        ngaySinh: '',
        diaChi: '',
        cccd: '',
        gioiTinh: ''
    });
    const [editMode, setEditMode] = useState(false);

    useEffect(() => {
        const storedUserInfo = localStorage.getItem('userInfo');
        if (storedUserInfo) {
            const parsedUserInfo = JSON.parse(storedUserInfo);
            console.log(parsedUserInfo)
            setUserInfo(parsedUserInfo);
            setEditData({
                hoTen: parsedUserInfo.hoTen || '',
                sdt: parsedUserInfo.sdt || '',
                ngaySinh: parsedUserInfo.ngaySinh ? formatDateForInput(parsedUserInfo.ngaySinh) : '',
                diaChi: parsedUserInfo.diaChi || '',
                cccd: parsedUserInfo.cccd || '',
                gioiTinh: parsedUserInfo.gioiTinh || ''
            });
        } else {
            console.log('Không tìm thấy thông tin người dùng trong localStorage.');
        }
    }, []);


    const formatDateForInput = (date) => {
        const d = new Date(date);
        const day = String(d.getDate()).padStart(2, '0');
        const month = String(d.getMonth() + 1).padStart(2, '0');
        const year = d.getFullYear();
        return `${year}-${month}-${day}`;
    };

    const handleInputChange = (field, value) => {
        if (field === 'ngaySinh') {
            value = formatDateForInput(value);
        }
        setEditData((prev) => ({
            ...prev,
            [field]: value,
        }));
    };


    const formatDate = (date) => {
        const d = new Date(date);
        const day = String(d.getDate()).padStart(2, '0');
        const month = String(d.getMonth() + 1).padStart(2, '0');
        const year = d.getFullYear();
        return `${day}/${month}/${year}`;
    };

    const handleSaveChanges = async () => {
        const updatedUserInfo = {
            maQL: userInfo.maQL,
            hoTen: editData.hoTen,
            sdt: editData.sdt,
            ngaySinh: editData.ngaySinh,
            diaChi: editData.diaChi,
            cccd: editData.cccd,
            gioiTinh: editData.gioiTinh
        };
        const data = await config.updateAbout(updatedUserInfo);

        if (data.errCode) {
            setInfoNotify({
                content: 'Lỗi dữ liệu !!',
                delay: 1500,
                isNotify: true,
                type: 'error',
            });
        } else {
            setInfoNotify({
                content: 'Cập nhật thành công !!',
                delay: 1500,
                isNotify: true,
                type: 'success',
            });
            setUserInfo(updatedUserInfo);
            localStorage.setItem('userInfo', JSON.stringify(updatedUserInfo));
            setEditMode(false);
        }

    };

    return (
        <Row className="justify-content-center">
            <Col md="6">
                <Card>
                    <CardBody>

                        {/* Avatar */}
                        <div className="text-center mb-4">
                            <img
                                src={user1 || '/default-avatar.png'}
                                alt="Avatar"
                                className="rounded-circle"
                                style={{ width: '150px', height: '150px', objectFit: 'cover' }}
                            />
                        </div>

                        {/* Tên */}
                        <div>
                            <label>Tên:</label>
                            {editMode ? (
                                <Input
                                    type="text"
                                    value={editData.hoTen}
                                    onChange={(e) => handleInputChange('hoTen', e.target.value)}
                                />
                            ) : (
                                <p>{userInfo.hoTen}</p>
                            )}
                        </div>

                        {/* Số điện thoại */}
                        <div>
                            <label>Số điện thoại:</label>
                            {editMode ? (
                                <Input
                                    type="text"
                                    value={editData.sdt}
                                    onChange={(e) => handleInputChange('sdt', e.target.value)}
                                />
                            ) : (
                                <p>{userInfo.sdt}</p>
                            )}
                        </div>

                        {/* Ngày sinh */}
                        <div>
                            <label>Ngày sinh:</label>
                            {editMode ? (
                                <Input
                                    type="date"
                                    value={editData.ngaySinh}
                                    onChange={(e) => handleInputChange('ngaySinh', e.target.value)}
                                />
                            ) : (
                                <p>{formatDate(userInfo.ngaySinh)}</p>
                            )}
                        </div>


                        {/* Địa chỉ */}
                        <div>
                            <label>Địa chỉ:</label>
                            {editMode ? (
                                <Input
                                    type="text"
                                    value={editData.diaChi}
                                    onChange={(e) => handleInputChange('diaChi', e.target.value)}
                                />
                            ) : (
                                <p>{userInfo.diaChi}</p>
                            )}
                        </div>

                        {/* CCCD */}
                        <div>
                            <label>CCCD:</label>
                            {editMode ? (
                                <Input
                                    type="text"
                                    value={editData.cccd}
                                    onChange={(e) => handleInputChange('cccd', e.target.value)}
                                />
                            ) : (
                                <p>{userInfo.cccd}</p>
                            )}
                        </div>

                        {/* Giới tính */}
                        <div>
                            <label>Giới tính:</label>
                            {editMode ? (
                                <Input
                                    type="select"
                                    value={editData.gioiTinh}
                                    onChange={(e) => handleInputChange('gioiTinh', e.target.value)}
                                >
                                    <option value="1">Nam</option>
                                    <option value="0">Nữ</option>
                                </Input>
                            ) : (
                                <p>{userInfo.gioiTinh === '1' ? 'Nam' : 'Nữ'}</p>
                            )}
                        </div>

                        {/* Save / Edit Button */}
                        <div className="text-center mt-4">
                            {editMode ? (
                                <Button color="primary" onClick={handleSaveChanges}><FaSave /> Lưu thay đổi</Button>
                            ) : (
                                <Button color="secondary" onClick={() => setEditMode(true)}><FaEdit /> Chỉnh sửa</Button>
                            )}
                        </div>
                    </CardBody>
                </Card>
            </Col>
        </Row>
    );
};

export default About;
