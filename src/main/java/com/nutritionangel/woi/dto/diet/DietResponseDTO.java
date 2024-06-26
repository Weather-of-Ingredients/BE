package com.nutritionangel.woi.dto.diet;

import com.nutritionangel.woi.dto.user.UserInfoResponseDTO;

import java.util.List;

public class DietResponseDTO {
    private Integer dietId;
    private String date;
    private String type;
    private String week;
    private List<MenuResponseDTO> menus;

    private UserInfoResponseDTO user;  // 사용자 정보 추가

    public UserInfoResponseDTO getUser() {  // 사용자 정보를 가져오는 메소드
        return user;
    }

    public void setUser(UserInfoResponseDTO user) {  // 사용자 정보를 설정하는 메소드
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getDietId() {
        return dietId;
    }

    public void setDietId(Integer dietId) {
        this.dietId = dietId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public List<MenuResponseDTO> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuResponseDTO> menus) {
        this.menus = menus;
    }
}
