package com.example.baihaqiapp.adapter;

import com.example.baihaqiapp.model.User;

public interface OnUserClickListener {
    void onEdit(User user);
    void onDelete(int userId);
}
