import { Card, CardBody, CardSubtitle, CardTitle } from 'reactstrap';
import Chart from 'react-apexcharts';

const SalesChart = ({ data }) => {
    // Xử lý dữ liệu: Mặc định giá trị 0 cho các tháng chưa có dữ liệu
    const processedData = Array.from({ length: 12 }, (_, index) => {
        const monthData = data.find((item) => item.Thang === index + 1);
        return monthData ? monthData.TongSoVe : 0;
    });

    const options = {
        chart: {
            toolbar: {
                show: false,
            },
            stacked: false,
        },
        dataLabels: {
            enabled: false,
        },
        stroke: {
            show: true,
            width: 4,
            colors: ['transparent'],
        },
        legend: {
            show: true,
        },
        plotOptions: {
            bar: {
                horizontal: false,
                columnWidth: '30%',
                borderRadius: 2,
            },
        },
        colors: ['#0d6efd', '#009efb', '#6771dc'],
        xaxis: {
            categories: [
                'Tháng 1',
                'Tháng 2',
                'Tháng 3',
                'Tháng 4',
                'Tháng 5',
                'Tháng 6',
                'Tháng 7',
                'Tháng 8',
                'Tháng 9',
                'Tháng 10',
                'Tháng 11',
                'Tháng 12',
            ],
        },
        responsive: [
            {
                breakpoint: 1024,
                options: {
                    plotOptions: {
                        bar: {
                            columnWidth: '60%',
                            borderRadius: 7,
                        },
                    },
                },
            },
        ],
    };
    const series = [
        {
            name: 'Số vé',
            data: processedData,
        },
    ];

    return (
        <Card>
            <CardBody>
                <CardTitle tag="h5">Tổng số vé được bán trong từng tháng</CardTitle>
                <CardSubtitle className="text-muted" tag="h6">
                    Biều đồ cột
                </CardSubtitle>
                <Chart options={options} series={series} type="bar" height="379" />
            </CardBody>
        </Card>
    );
};

export default SalesChart;
