import React, { useState, useEffect } from 'react';
import { Row, Col, Card, CardBody, CardTitle, Button, Input, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';
import { FaSave, FaEdit, FaCamera } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';
import config from '~/services';

const About = () => {
    const [userInfo, setUserInfo] = useState({});
    const [editData, setEditData] = useState({ hoTen: '', sdt: '', ngaySinh: '', email: ''});
    const [editMode, setEditMode] = useState(false);

    const navigate = useNavigate();

    useEffect(() => {
     
        const storedUserInfo = localStorage.getItem('userInfo');
        console.log(storedUserInfo)
        if (storedUserInfo) {
            const parsedUserInfo = JSON.parse(storedUserInfo);
            setUserInfo(parsedUserInfo);
            console.log(parsedUserInfo)
            setEditData({
                hoTen: parsedUserInfo.hoTen || '',
                sdt: parsedUserInfo.sdt || '',
                ngaySinh: parsedUserInfo.ngaySinh || '',
                email: parsedUserInfo.email || '',
               
            });
        } else {
            console.log('Không tìm thấy thông tin người dùng trong localStorage.');
        }
    }, []);

    const handleInputChange = (field, value) => {
        setEditData((prev) => ({
            ...prev,
            [field]: value,
        }));
    };

    const handleSaveChanges = async () => {
        const updatedUserInfo = {
            maKH: userInfo.maKH,
            hoTen: editData.hoTen,
            sdt: editData.sdt,
            ngaySinh: new Date(editData.ngaySinh).toISOString(),
            email: editData.email
        };
        setUserInfo(updatedUserInfo);
        console.log('Dữ liệu chuẩn bị gửi:', updatedUserInfo);
        localStorage.setItem('userInfo', JSON.stringify(updatedUserInfo));

        try {
           
            const response = await config.updateAbout(userInfo.maKH, updatedUserInfo);
            console.log('Dữ liệu trả về từ API:', response);
            console.log('Dữ liệu đã cập nhật thành công:', response.data);
           
           
        } catch (error) {
            console.error('Có lỗi xảy ra khi cập nhật thông tin:', error);
        }
        
        setEditMode(false);
    };

    return (
        <Row className="justify-content-center">
            <Col md="6">
                <Card>
                    <CardBody>
                        <CardTitle tag="h5" className="text-center">Thông tin cá nhân</CardTitle>
                       
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
                        <div>
                            <label>Số điện thoại:</label>
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
                        <div>
                            <label>Ngày sinh:</label>
                            {editMode ? (
                                <Input
                                    type="text"
                                    value={editData.ngaySinh}
                                    onChange={(e) => handleInputChange('ngaySinh', e.target.value)}
                                />
                            ) : (
                                <p>{userInfo.ngaySinh}</p>
                            )}
                        </div>
                        <div>
                            <label>email:</label>
                            {editMode ? (
                                <Input
                                    type="text"
                                    value={editData.email}
                                    onChange={(e) => handleInputChange('email', e.target.value)}
                                />
                            ) : (
                                <p>{userInfo.email}</p>
                            )}
                        </div>
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
