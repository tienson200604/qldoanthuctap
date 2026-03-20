package com.web.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping(value = {"/add-blog"}, method = RequestMethod.GET)
    public String addBlog() {
        return "admin/add-blog";
    }

    @RequestMapping(value = {"/add-category"}, method = RequestMethod.GET)
    public String addCategory() {
        return "admin/add-category";
    }

    @RequestMapping(value = {"/add-company"}, method = RequestMethod.GET)
    public String addCompany() {
        return "admin/add-company";
    }

    @RequestMapping(value = {"/add-company-semester"}, method = RequestMethod.GET)
    public String addCompanySemester() {
        return "admin/add-company-semester";
    }

    @RequestMapping(value = {"/add-document"}, method = RequestMethod.GET)
    public String addDocument() {
        return "admin/add-document";
    }

    @RequestMapping(value = {"/add-score"}, method = RequestMethod.GET)
    public String addScore() {
        return "admin/add-score";
    }

    @RequestMapping(value = {"/add-semester"}, method = RequestMethod.GET)
    public String addSemester() {
        return "admin/add-semester";
    }

    @RequestMapping(value = {"/add-semester-type"}, method = RequestMethod.GET)
    public String addSemesterType() {
        return "admin/add-semester-type";
    }

    @RequestMapping(value = {"/add-user"}, method = RequestMethod.GET)
    public String addUser() {
        return "admin/add-user";
    }

    @RequestMapping(value = {"/blog"}, method = RequestMethod.GET)
    public String blog() {
        return "admin/blog";
    }

    @RequestMapping(value = {"/category"}, method = RequestMethod.GET)
    public String category() {
        return "admin/category";
    }

    @RequestMapping(value = {"/company"}, method = RequestMethod.GET)
    public String company() {
        return "admin/company";
    }

    @RequestMapping(value = {"/company-semester"}, method = RequestMethod.GET)
    public String companySemester() {
        return "admin/company-semester";
    }

    @RequestMapping(value = {"/document"}, method = RequestMethod.GET)
    public String document() {
        return "admin/document";
    }

    @RequestMapping(value = {"/doi-mat-khau"}, method = RequestMethod.GET)
    public String doiMatKhau() {
        return "admin/doimatkhau";
    }


    @RequestMapping(value = {"/khung"}, method = RequestMethod.GET)
    public String khung() {
        return "admin/khung";
    }

    @RequestMapping(value = {"/log"}, method = RequestMethod.GET)
    public String log() {
        return "admin/log";
    }

    @RequestMapping(value = {"/score"}, method = RequestMethod.GET)
    public String score() {
        return "admin/score";
    }

    @RequestMapping(value = {"/semester"}, method = RequestMethod.GET)
    public String semester() {
        return "admin/semester";
    }

    @RequestMapping(value = {"/semester-type"}, method = RequestMethod.GET)
    public String semesterType() {
        return "admin/semester-type";
    }

    @RequestMapping(value = {"/user"}, method = RequestMethod.GET)
    public String user() {
        return "admin/user";
    }
}