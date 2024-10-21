package com.example.appmoviednk.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.appmoviednk.MovieSession;
import com.example.appmoviednk.R;
import com.example.appmoviednk.UserSession;
import com.example.appmoviednk.activity.MainActivity;
import com.example.appmoviednk.adapter.BookChairAdapter;
import com.example.appmoviednk.adapter.ScheduleShowingAdapter;
import com.example.appmoviednk.databinding.ButtonPrimaryBinding;
import com.example.appmoviednk.databinding.FragmentBookTicketBinding;
import com.example.appmoviednk.model.BookChairModel;
import com.example.appmoviednk.model.BookTicketModel;
import com.example.appmoviednk.model.CustomerModel;
import com.example.appmoviednk.model.DateShowingModel;
import com.example.appmoviednk.model.MovieModel;
import com.example.appmoviednk.retrofit.RetrofitClient;
import com.example.appmoviednk.service.ApiService;
import com.example.appmoviednk.service.BookService;
import com.example.appmoviednk.service.MovieTicketService;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
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

    BookService bookService;
    MovieTicketService movieTicketService;
    List<BookChairModel> selectedChairs = new ArrayList<>();

    double totalAmount = 0.0;
    double priceSeat = 0;
    String maVe;
    List<String> maGhe;

    BookTicketModel bookTicketModel = new BookTicketModel();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate FragmentBookTicketBinding
        binding = FragmentBookTicketBinding.inflate(inflater, container, false);

        bookChairAdapter = new BookChairAdapter(requireContext(), seat -> {
            if (seat.isTinhTrang()) return; // Nếu ghế đã bán, không làm gì

            if (selectedChairs.contains(seat)) {
                selectedChairs.remove(seat); // Bỏ chọn ghế
                totalAmount -= priceSeat;
                Log.d("Selected Chairs", "Removed: " + seat.getMaGhe());
            } else {
                selectedChairs.add(seat); // Chọn ghế
                totalAmount += priceSeat;
                MovieModel selectedMovie = MovieSession.getInstance().getSelectedMovie();
                Log.d("Succccessss",selectedMovie.getMaPhim());
                Log.d("Selected Chairs", "Added: " + seat.getMaGhe());
            }
           // Cập nhật
            binding.totalAmountTv.setText("Tổng tiền: " + totalAmount + " VND");
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 3);
        binding.recyclerViewSeats.setLayoutManager(gridLayoutManager);

        binding.recyclerViewSeats.setAdapter(bookChairAdapter);

        bookService = RetrofitClient.getRetrofitInstance().create(BookService.class);
        Log.d("ma suat dc chon", getArguments().getString("maSuat"));
        renderChairs(getArguments().getString("maSuat"));

        movieTicketService = RetrofitClient.getRetrofitInstance().create(MovieTicketService.class);
        getTicketsByMovie(getArguments().getString("maPhim"));


                // Sử dụng đối tượng MovieModel trong BookTicketFragment




        binding.btnBook.btnPrimary.setText("Đặt vé");
        // Đặt sự kiện cho nút btnBookTicket
        binding.btnBook.btnPrimary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loggedInAccount = UserSession.getInstance().getLoggedInAccount();
                if(selectedChairs.isEmpty()){
                    Log.d("thong bao: ","khong co ghe nao duoc chon");
                }
                else if (loggedInAccount!=null){
                    showSelectedChairsDialog();
                    Log.d("thong tin", maVe + ", " +getArguments().getString("maSuat"));
                    setBookticket();
                    Log.d("book ticket", bookTicketModel +"");
                } else {
                    Log.d("thong bao","ban phai dang nhap moi dat ve");
                }
            }
        });

        binding.selectVoucherBtn.btnPrimary.setText("Sử dụng");
        binding.btnBack.setOnClickListener(view -> {
            handleBackButtonClick();
        });
        return binding.getRoot();
    }

    private void handleBackButtonClick() {
        // Gọi onBackPressed() của MainActivity
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).onBackPressed();
        }
    }

    private void getTicketsByMovie(String maPhim) {
        Call<List<Map<String, Object>>> call = movieTicketService.getTicketsByMovie(maPhim);
        call.enqueue(new Callback<List<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {
                if (response.isSuccessful()) {
                    List<Map<String, Object>> tickets = response.body();
                    for (Map<String, Object> ticket : tickets) {
                        // Xử lý từng vé

                        maVe = (String) ticket.get("maVe");
                        String maPhim = (String) ticket.get("maPhim");
                        priceSeat = (Double) ticket.get("tien");

                        Log.d("Ticket Info", "Ma ve: " + maVe + ", Ma phim: " + maPhim + " tien: " + priceSeat
                        );
                    }
                } else {
                    Toast.makeText(getContext(), "Loiii1", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                Toast.makeText(getContext(), "Loiiii: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setBookticket() {
        bookTicketModel.setMaSuat(getArguments().getString("maSuat"));
        bookTicketModel.setMaKH(loggedInAccount.getMaKH());
        bookTicketModel.setMaVe(maVe);
        bookTicketModel.setTongTien((int) totalAmount);

        maGhe = new ArrayList<>();
        for (BookChairModel chair : selectedChairs) {
            maGhe.add(chair.getMaGhe());
        }



    }

    private void renderChairs(String maSuat){
        bookService.getGheDaDat(maSuat).enqueue(new Callback<List<BookChairModel>>() {
            @Override
            public void onResponse(Call<List<BookChairModel>> call, Response<List<BookChairModel>> response) {
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
            public void onFailure(Call<List<BookChairModel>> call, Throwable t) {
                Log.d("API aaa3", t.getMessage());
            }
        });
    }

    private void insertSelectedChairs(String maBook, List<String> maGheList) {
        Map<String, Object> bookGheData = new HashMap<>();
        bookGheData.put("maBook", maBook);
        bookGheData.put("maGhe", maGhe);


        Log.d("BookGheData", bookGheData.toString());
        movieTicketService.insertBookGhe(bookGheData).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("Request Data", "maBook: " + maBook + ", maGheList: " + maGheList);
                if (response.isSuccessful()) {
                    Log.d("okkkk","11111111");
                } else {
                    // Xử lý lỗi nếu không thành công
                    Log.e("API aaa32", "Error code: " + response.code() + ", message: " + response.message());
                    Toast.makeText(getContext(), "Lỗi: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Xử lý lỗi kết nối hoặc lỗi không thành công
                Log.e("API Error", t.getMessage());
            }
        });
    }


    private void showSelectedChairsDialog() {
        StringBuilder selectedChairsString = new StringBuilder("Ghế đã chọn: ");
        for (BookChairModel chair : selectedChairs) {
            selectedChairsString.append(chair.getMaGhe()).append(" ");
        }
        selectedChairsString.append("\nTổng số tiền: ").append(totalAmount);
        new AlertDialog.Builder(requireContext())
                .setTitle("Thông báo")
                .setMessage(selectedChairsString.toString())

                .setPositiveButton("Đặt vé", (dialog, which) -> {
                    // Thực hiện đặt vé
                    movieTicketService.insertBookVe(bookTicketModel).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            Log.d("API Response", "Response Code: " + response.code());
                            if (response.isSuccessful() && response.body() != null) {
                                JsonObject jsonResponse = response.body();

                                // Lấy thông tin từ JsonObject
                                String maBook = jsonResponse.get("maBook").getAsString();
                                String message = jsonResponse.get("message").getAsString();

                                Log.d("maBook", maBook);
                                Toast.makeText(getContext(), message + " Mã vé: " + maBook, Toast.LENGTH_SHORT).show();

                                insertSelectedChairs(maBook, maGhe);
                                // Chuyển tới SuccessFragment sau khi đặt vé thành công
                                SuccessFragment successFragment = new SuccessFragment();
                                ((MainActivity) getActivity()).replaceFragment(successFragment);
                            } else {
                                // Xử lý lỗi nếu không thành công
                                Log.e("API aaa1", "Error code: " + response.code() + ", message: " + response.message());
                                Toast.makeText(getContext(), "Lỗi: " + response.message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Log.e("aaaa2", "Failure: " + t.getMessage(), t);
                            Toast.makeText(getContext(), "Thất bại: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
//
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
