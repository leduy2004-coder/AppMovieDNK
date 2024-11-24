import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
    Container,
    Row,
    Col,
    Form,
    FormGroup,
    Label,
    Input,
    Button,
    Card,
    CardBody,
    CardTitle,
    CardText,
} from 'reactstrap';
import Notify from '~/components/Notify';
import config from '~/services';
import { UserNotify } from '~/components/store/NotifyContext';

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const { setInfoNotify } = UserNotify();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        if (!username || !password) {
            setError('Cả tài khoản và mật khẩu đều bắt buộc!');
            return;
        }

        setIsLoading(true);

        try {
            const response = await config.login(username, password);
            if (response.errCode) {
                setInfoNotify({
                    content: 'Đăng xuất thất bại Vui lòng kiểm tra lại !!',
                    delay: 1500,
                    isNotify: true,
                    type: 'error',
                });
            } else {
                navigate('/starter');
                localStorage.setItem('userInfo', JSON.stringify(response));
                setInfoNotify({
                    content: 'Đăng nhập thành công !!',
                    delay: 1500,
                    isNotify: true,
                    type: 'success',
                });
            }
        } catch (err) {
            console.error('Lỗi khi gọi API:', err);
            setError('Đã xảy ra lỗi khi đăng nhập. Vui lòng thử lại!');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div
            className="d-flex justify-content-center align-items-center"
            style={{ height: '100vh', backgroundColor: '#f8f9fa' }}
        >
            <Container>
                <Row className="justify-content-center">
                    <Col sm="12" md="6" lg="4">
                        <Card>
                            <CardBody>
                                <CardTitle tag="h5" className="text-center">
                                    Đăng nhập
                                </CardTitle>
                                <CardText className="text-center mb-4">Hãy điền đầy đủ thông tin</CardText>
                                <Form onSubmit={handleSubmit}>
                                    {error && <div className="alert alert-danger">{error}</div>}
                                    <FormGroup>
                                        <Label for="username">Tài khoản</Label>
                                        <Input
                                            type="text"
                                            name="username"
                                            id="username"
                                            placeholder="Nhập tài khoản"
                                            value={username}
                                            onChange={(e) => setUsername(e.target.value)}
                                            required
                                        />
                                    </FormGroup>
                                    <FormGroup>
                                        <Label for="password">Mật khẩu</Label>
                                        <Input
                                            type="password"
                                            name="password"
                                            id="password"
                                            placeholder="Nhập mật khẩu"
                                            value={password}
                                            onChange={(e) => setPassword(e.target.value)}
                                            required
                                            autoComplete="current-password"
                                        />
                                    </FormGroup>
                                    <Button color="dark" block type="submit">
                                        Đăng nhập
                                    </Button>
                                </Form>
                            </CardBody>
                        </Card>
                    </Col>
                </Row>
            </Container>

            <Notify />
        </div>
    );
};

export default Login;
