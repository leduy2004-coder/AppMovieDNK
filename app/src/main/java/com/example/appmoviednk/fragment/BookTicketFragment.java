package com.example.appmoviednk.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.appmoviednk.R;
import com.example.appmoviednk.UserSession;
import com.example.appmoviednk.activity.MainActivity;
import com.example.appmoviednk.adapter.BookChairAdapter;
import com.example.appmoviednk.adapter.SpinnerAdapter;
import com.example.appmoviednk.databinding.ButtonPrimaryBinding;
import com.example.appmoviednk.databinding.FragmentBookTicketBinding;
import com.example.appmoviednk.model.BookChairModel;
import com.example.appmoviednk.model.BookTicketModel;
import com.example.appmoviednk.model.CustomerModel;
import com.example.appmoviednk.model.MovieModel;
import com.example.appmoviednk.model.SharedViewModel;
import com.example.appmoviednk.model.ShiftModel;
import com.example.appmoviednk.retrofit.RetrofitClient;
import com.example.appmoviednk.service.BookService;
import com.example.appmoviednk.service.CustomerService;
import com.example.appmoviednk.service.MovieTicketService;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookTicketFragment extends Fragment {

    FragmentBookTicketBinding binding;
    ButtonPrimaryBinding buttonPrimaryBinding;
    BookChairAdapter bookChairAdapter;
    CustomerModel loggedInAccount;
    SharedViewModel sharedViewModel;
    ShiftModel shiftModel;
    MovieModel movieModel;

    BookService bookService;
    CustomerService customerService;
    MovieTicketService movieTicketService;
    List<BookChairModel> selectedChairs = new ArrayList<>();

    double totalAmount = 0.0;
    double priceSeat = 0;
    String maVe;
    String maSuat;
    String maPhim;
    List<String> maGhe;

    int voucherSelect;
    int pointSelect;
    int point;
    int voucherCount;
    BookTicketModel bookTicketModel = new BookTicketModel();

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Khởi tạo FragmentBookTicketBinding
        binding = FragmentBookTicketBinding.inflate(inflater, container, false);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        shiftModel = new ViewModelProvider(requireActivity()).get(ShiftModel.class);
        movieModel = new ViewModelProvider(requireActivity()).get(MovieModel.class);
        bookService = RetrofitClient.getRetrofitInstance().create(BookService.class);
        movieTicketService = RetrofitClient.getRetrofitInstance().create(MovieTicketService.class);
        customerService = RetrofitClient.getRetrofitInstance().create(CustomerService.class);


        bookChairAdapter = new BookChairAdapter(requireContext(), seat -> {
            if (seat.isTinhTrang()) return; // Nếu ghế đã bán, không làm gì

            if (selectedChairs.contains(seat)) {
                selectedChairs.remove(seat); // Bỏ chọn ghế
                totalAmount -= priceSeat;

                Log.d("Selected Chairs", "Removed: " + seat.getMaGhe());
            } else {
                selectedChairs.add(seat); // Chọn ghế
                totalAmount += priceSeat;
//                selectedChairsString.append(seat.getMaGhe()).append(" ");
                Log.d("Selected Chairs", "Added: " + seat.getMaGhe());

            }
            StringBuilder selectedChairsString = new StringBuilder("Ghế đang chọn: ");
            for (BookChairModel s : selectedChairs) {
                selectedChairsString.append(s.getMaGhe()).append(" "); // Thêm ghế còn lại vào chuỗi
            }
            binding.infSeats.setText(selectedChairsString.toString());
            // Cập nhật tổng tiền
            binding.totalAmountTv.setText("Tổng tiền: " + totalAmount + " VND");
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 3);
        binding.recyclerViewSeats.setLayoutManager(gridLayoutManager);
        binding.recyclerViewSeats.setAdapter(bookChairAdapter);

        shiftModel.getSelectedShift().observe(getViewLifecycleOwner(), new Observer<ShiftModel>() {
            @Override
            public void onChanged(ShiftModel shift) {
                if (shift != null) {
                    maSuat = shift.getMaSuat();
                    renderChairs(shift.getMaSuat());
                    Log.d("Suat Chieu", maSuat);
                } else {
                    Log.d("MovieFragment", "Nhận được MovieModel null");
                }
            }
        });

        movieModel.getSelectedMovie().observe(getViewLifecycleOwner(), new Observer<MovieModel>() {
            @Override
            public void onChanged(MovieModel selectedMovie) {
                if (selectedMovie != null) {
                    maPhim = selectedMovie.getMaPhim();
                    getTicketsByMovie(selectedMovie.getMaPhim());
                    Log.d("Suat Chieu", maPhim);
                } else {
                    Log.d("MovieFragment", "Nhận được MovieModel null");
                }
            }
        });
        getVoucherAndPointFromApi();
        //Click chon ma giam gia
        binding.selectVoucherBtn.btnPrimary.setOnClickListener(view -> {
            if (binding.selectVoucherBtn.btnPrimary.getText().toString().equals("Sử dụng")){
                showVerificationDialog();
            }else {
                deleteVoucher();
            }
        });

        //Click chon dùng điểm
        binding.selectPointBtn.btnPrimary.setOnClickListener(view -> {
            if (binding.selectPointBtn.btnPrimary.getText().toString().equals("Sử dụng")){
                showVerifiUsePointDialog();
            }else {
                deleteUsePoint();
            }
        });

        binding.btnBook.btnPrimary.setText("Đặt vé");
        //click dat ve
        binding.btnBook.btnPrimary.setOnClickListener(view -> {
            loggedInAccount = UserSession.getInstance().getLoggedInAccount();
            if (selectedChairs.isEmpty()) {
                Log.d("Thông báo: ", "Không có ghế nào được chọn");
                Toast.makeText(getContext(), "Không có ghế nào được chọn", Toast.LENGTH_SHORT).show();
            } else if (loggedInAccount != null) {
                showSelectedChairsDialog();

                setBookticket(maSuat);
                Log.d("book ticket", bookTicketModel + "");
            } else {
                diaLogLogin();
            }
        });

        binding.selectPointBtn.btnPrimary.setText("Sử dụng");
        binding.selectVoucherBtn.btnPrimary.setText("Sử dụng");
        binding.btnBack.setOnClickListener(view -> handleBackButtonClick());

        return binding.getRoot();
    }

    private void handleBackButtonClick() {
        // Gọi onBackPressed() của MainActivity
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).onBackPressed();
        }
    }

    // Lấy thông tin vé
    private void getTicketsByMovie(String maPhim) {
        Call<List<Map<String, Object>>> call = movieTicketService.getTicketsByMovie(maPhim);
        call.enqueue(new Callback<List<Map<String, Object>>>() {
            @Override
            public void onResponse(@NonNull Call<List<Map<String, Object>>> call, @NonNull Response<List<Map<String, Object>>> response) {
                if (response.isSuccessful()) {
                    List<Map<String, Object>> tickets = response.body();
                    assert tickets != null;
                    for (Map<String, Object> ticket : tickets) {
                        // Xử lý từng vé
                        maVe = (String) ticket.get("maVe");
                        priceSeat = (Double) ticket.get("tien");
                        Log.d("Ticket Info", "Mã vé: " + maVe + ", Giá: " + priceSeat);
                    }
                } else {
                    Log.d("loi nae ca", "onResponse: ");
                    Toast.makeText(getContext(), "Lỗi khi lấy thông tin vé", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Map<String, Object>>> call, @NonNull Throwable t) {
                Log.d("API Error ma phim", t.getMessage());
            }
        });
    }

    // Lấy thông tin voucher và điểm
    private void getVoucherAndPointFromApi() {
        if (UserSession.getInstance().getLoggedInAccount() == null) {
            diaLogLogin();
        } else {
            customerService.getSoLuongVoucherVaDiem(UserSession.getInstance().getLoggedInAccount().getMaKH()).enqueue(new Callback<CustomerModel>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NonNull Call<CustomerModel> call, @NonNull Response<CustomerModel> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        CustomerModel customerModel = response.body();
                        voucherCount = customerModel.getSoLuongVoucher();
                        point = customerModel.getDiemThuong();
                        binding.tvPoint.setText("Điểm: " + customerModel.getDiemThuong());
                        binding.tvVoucher.setText("Voucher: " + customerModel.getSoLuongVoucher());
                    } else {
                        Log.e("API_ERROR", "Không lấy được danh sách phim: " + response.errorBody());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CustomerModel> call, @NonNull Throwable t) {
                    Log.e("API_ERROR", "Lỗi khi gọi API: " + t.getMessage(), t);
                }
            });
        }
    }

    // Thiết lập dữ liệu để đặt vé
    private void setBookticket(String maSuat) {
        bookTicketModel.setMaSuat(maSuat);
        bookTicketModel.setMaKH(loggedInAccount.getMaKH());
        bookTicketModel.setMaVe(maVe);
        bookTicketModel.setTongTien((int) totalAmount);
        bookTicketModel.setSuDungVoucher(voucherSelect);
        maGhe = new ArrayList<>();
        for (BookChairModel chair : selectedChairs) {
            maGhe.add(chair.getMaGhe());
        }
    }

    // Render các ghế: chưa đặt, đã đặt
    private void renderChairs(String maSuat) {
        bookService.getGheDaDat(maSuat).enqueue(new Callback<List<BookChairModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<BookChairModel>> call, @NonNull Response<List<BookChairModel>> response) {
                if (response.isSuccessful()) {
                    List<BookChairModel> bookSeats = response.body();
                    List<BookChairModel> chairList = new ArrayList<>();
                    for (char row = 'A'; row <= 'C'; row++) {
                        for (int i = 1; i <= 3; i++) {
                            String seatCode = row + String.valueOf(i);
                            boolean isBooked = false;

                            // Kiểm tra xem ghế đã được đặt hay chưa
                            for (BookChairModel bookedSeat : bookSeats) {
                                if (bookedSeat.getMaGhe().equals(seatCode)) {
                                    isBooked = true;
                                    break;
                                }
                            }
                            chairList.add(new BookChairModel(seatCode, isBooked));
                        }
                    }
                    bookChairAdapter.setData(chairList);
                } else {
                    Log.d("Book", "Response body is null");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<BookChairModel>> call, @NonNull Throwable t) {
                Log.d("API Error ma suat", t.getMessage());
            }
        });
    }

    //     Insert  ghe da dat
    private void insertSelectedChairs(String maBook, List<String> maGheList) {
        Map<String, Object> bookGheData = new HashMap<>();
        bookGheData.put("maBook", maBook);
        bookGheData.put("maGhe", maGhe);

        Log.d("BookGheData", bookGheData.toString());
        movieTicketService.insertBookGhe(bookGheData).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                Log.d("Request Data", "maBook: " + maBook + ", maGheList: " + maGheList);
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Đặt ghế thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("Insert Selected Chair Error", response.message());
                    Toast.makeText(getContext(), "Đặt ghế thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.e("Error", t.getMessage());
                Toast.makeText(getContext(), "Đặt ghế thất bại" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Insert thông tin đặt vé
    private void insertBookVe() {
        movieTicketService.insertBookVe(bookTicketModel).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Đặt vé thành công", Toast.LENGTH_SHORT).show();
                    String maBook = response.body().get("maBook").getAsString();
                    insertSelectedChairs(maBook, maGhe);

                    SuccessFragment successFragment = new SuccessFragment();
                    Bundle bundle = new Bundle();
                    bundle.putDouble("totalAmount", totalAmount);
                    bundle.putInt("voucher", voucherCount - voucherSelect);
                    bundle.putInt("point", point - pointSelect);
                    bundle.putString("maBook", maBook);

                    bundle.putString("maKH", UserSession.getInstance().getLoggedInAccount().getMaKH());
                    successFragment.setArguments(bundle);
                    ((MainActivity) getActivity()).replaceFragment(successFragment, true);
                } else {
                    Log.e("Insert BookVe Error", response.message());
                    Toast.makeText(getContext(), "Đặt vé thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Error", t.getMessage());
                Toast.makeText(getContext(), "Đặt vé thất bại" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hiện dialog để xác nhận ghế đã chọn
    private void showSelectedChairsDialog() {
        StringBuilder selectedChairsString = new StringBuilder("Ghế đã chọn: ");
        for (BookChairModel chair : selectedChairs) {
            selectedChairsString.append(chair.getMaGhe()).append(" ");
        }

        selectedChairsString.append("\nTổng số tiền: ").append(totalAmount);
        new AlertDialog.Builder(requireContext())
                .setTitle("Ghế đã chọn")
                .setMessage(selectedChairsString.toString())
                .setPositiveButton("Xác nhận", (dialog, which) -> {
                    insertBookVe(); // Gọi hàm để đặt vé
                    Log.d("Selected Chairs", selectedChairsString.toString());
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }
    // Hiện dialog để xác nhận dung diem
    @SuppressLint("SetTextI18n")
    private void showVerifiUsePointDialog() {
        if(point == 0){
            Toast.makeText(getContext(), "Bạn không có điểm nào", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedChairs.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng chọn ghế", Toast.LENGTH_SHORT).show();
            return;
        }
        // 1 điểm bằng 1000 đồng
        if (point * 1000 < totalAmount) {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Bạn có muốn sử dùng điểm không?")
                    .setMessage("Sẽ không còn điểm nếu dùng.")
                    .setPositiveButton("Xác nhận", (dialog, which) -> {
                        // Sử dụng toàn bộ điểm
                        pointSelect = point;

                        binding.tvPoint.setText("Đã sử dụng hết điểm");
                        binding.selectPointBtn.btnPrimary.setText("Hủy");
                        totalAmount = totalAmount - pointSelect * 1000;
                        // Cập nhật tổng tiền
                        binding.totalAmountTv.setText("Tổng tiền: " + totalAmount + " VND");
                    })
                    .setNegativeButton("Huỷ", null)
                    .show();
        } else {
            int remainingPoints = point - (int) (totalAmount / 1000); // Tính số điểm còn lại
            new AlertDialog.Builder(requireContext())
                    .setTitle("Bạn có muốn sử dùng điểm không?")
                    .setMessage("Sẽ còn " + remainingPoints + " điểm nếu dùng.")
                    .setPositiveButton("Xác nhận", (dialog, which) -> {
                        // Sử dụng đúng số điểm cần thiết
                        pointSelect = (int) (totalAmount / 1000);

                        binding.tvPoint.setText("Còn "+ remainingPoints +" điểm");
                        binding.selectPointBtn.btnPrimary.setText("Hủy");
                        totalAmount = 0;
                        // Cập nhật tổng tiền
                        binding.totalAmountTv.setText("Tổng tiền: " + totalAmount + " VND");
                    })
                    .setNegativeButton("Huỷ", null)
                    .show();
        }

    }
    // Hiện dialog để chọn số lượng voucher
    private void showVerificationDialog() {
        if(voucherCount == 0){
            Toast.makeText(getContext(), "Bạn không có voucher nào", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedChairs.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng chọn ghế", Toast.LENGTH_SHORT).show();
            return;
        }
        // Tạo một hộp thoại
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_use_voucher, null);
        builder.setView(dialogView);

        // Khai báo các thành phần trong hộp thoại
        TextView tvQuestion = dialogView.findViewById(R.id.tvQuestion);
        NumberPicker numberPicker = dialogView.findViewById(R.id.numberPicker);
        Button btnConfirm = dialogView.findViewById(R.id.success_btn);
        Button btnExit = dialogView.findViewById(R.id.exit_btn);

        numberPicker.setMaxValue(Math.min((int) (totalAmount / 1000), voucherCount));
        numberPicker.setMinValue(0);
        // Tạo hộp thoại
        AlertDialog dialog = builder.create();

        // Xử lý sự kiện khi nhấn nút xác nhận
        btnConfirm.setOnClickListener(v -> {
            int voucherCount = numberPicker.getValue(); // Lấy số lượng voucher từ NumberPicker
            if (voucherCount > 0) {
                // Gọi hàm xử lý số lượng voucher (Giả sử bạn có hàm này)
                handleVoucherCount(voucherCount);
                dialog.dismiss(); // Đóng hộp thoại
            } else {
                Toast.makeText(getContext(), "Vui lòng chọn số lượng voucher hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện khi nhấn nút thoát
        btnExit.setOnClickListener(v -> dialog.dismiss()); // Đóng hộp thoại khi thoát

        dialog.show(); // Hiện hộp thoại
    }

    // Hàm xử lý số lượng voucher được chọn
    @SuppressLint("SetTextI18n")
    private void handleVoucherCount(int voucher) {
        voucherSelect = voucher;
        binding.tvVoucher.setText("Đã sử dụng "+ voucherSelect+ " voucher");
        binding.selectVoucherBtn.btnPrimary.setText("Hủy");
        totalAmount = totalAmount - voucherSelect * 1000;
        // Cập nhật tổng tiền
        binding.totalAmountTv.setText("Tổng tiền: " + totalAmount + " VND");
    }

    // Hàm xóa voucher được chọn
    @SuppressLint("SetTextI18n")
    private void deleteVoucher(){
        totalAmount = totalAmount + voucherSelect * 1000;
        binding.selectVoucherBtn.btnPrimary.setText("Sử dụng");
        binding.totalAmountTv.setText("Tổng tiền: " + totalAmount + " VND");
        binding.tvVoucher.setText("Voucher: " + voucherCount);
        voucherSelect = 0;
    }

    // Hàm xóa diem
    @SuppressLint("SetTextI18n")
    private void deleteUsePoint(){
        totalAmount = totalAmount + pointSelect * 1000;
        binding.selectPointBtn.btnPrimary.setText("Sử dụng");
        binding.totalAmountTv.setText("Tổng tiền: " + totalAmount + " VND");
        binding.tvPoint.setText("Điểm hiện có: " + point);
        pointSelect = 0;
    }
    // Hiện dialog yêu cầu đăng nhập
    private void diaLogLogin() {
        new AlertDialog.Builder(getContext())
                .setTitle("Đăng nhập")
                .setMessage("Bạn cần đăng nhập để đặt vé.")
                .setPositiveButton("Đăng nhập", (dialog, which) -> {
                    LoginFragment loginFragment = new LoginFragment();
                    Bundle args = new Bundle();
                    args.putBoolean("returnToBookTicket", true); // Truyền tham số để quay lại BookTicketFragment
                    loginFragment.setArguments(args);

                    MainActivity mainActivity = (MainActivity) getActivity();
                    if (mainActivity != null) {
                        mainActivity.replaceFragment(loginFragment, true);
                    }
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Giải phóng binding để tránh rò rỉ bộ nhớ
    }
}