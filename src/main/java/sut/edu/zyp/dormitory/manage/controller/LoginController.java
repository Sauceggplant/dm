package sut.edu.zyp.dormitory.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import sut.edu.zyp.dormitory.manage.dto.LoginRequest;
import sut.edu.zyp.dormitory.manage.dto.LoginResponse;
import sut.edu.zyp.dormitory.manage.dto.LogoutRequest;
import sut.edu.zyp.dormitory.manage.dto.LogoutResponse;
import sut.edu.zyp.dormitory.manage.entity.AdminEntity;
import sut.edu.zyp.dormitory.manage.entity.DormitoryManagerEntity;
import sut.edu.zyp.dormitory.manage.entity.StudentEntity;
import sut.edu.zyp.dormitory.manage.enums.ResponseCodeEnum;
import sut.edu.zyp.dormitory.manage.repository.AdminRepository;
import sut.edu.zyp.dormitory.manage.repository.DormitoryManagerRepository;
import sut.edu.zyp.dormitory.manage.repository.StudentRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 登录控制器
 *
 * @author zyp
 * @version 0.0.1
 * @since 0.0.1
 */
@Controller
public class LoginController {

    /**
     * 验证码有效时间
     * 60秒
     */
    private static final long cpachaTimeout = 86400000;

    /**
     * 超级管理员存储服务
     */
    @Autowired
    private AdminRepository adminRepository;

    /**
     * 学生存储服务
     */
    @Autowired
    private StudentRepository studentRepository;

    /**
     * 宿管存储服务
     */
    @Autowired
    private DormitoryManagerRepository dormitoryManagerRepository;

    @RequestMapping(method = RequestMethod.POST, path = "/login", produces = "application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        try {
            LoginResponse loginResponse = doLogin(loginRequest, request);
            return new ResponseEntity(loginResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "/logout", produces = "application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<LogoutResponse> logout(@RequestBody LogoutRequest logoutRequest, HttpServletRequest request) {
        try {
            LogoutResponse logoutResponse = doLogout(logoutRequest, request);
            return new ResponseEntity(logoutResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public LogoutResponse doLogout(LogoutRequest logoutRequest, HttpServletRequest request) {
        LogoutResponse logoutResponse = new LogoutResponse();
        logoutResponse.setOperator(logoutRequest.getOperator());
        logoutResponse.setRequestId(logoutRequest.getRequestId());
        request.getSession().setAttribute("user", null);
        request.getSession().setAttribute("userType", null);
        logoutResponse.setResponseCode(ResponseCodeEnum.SUCCESS);
        logoutResponse.setTimestamp(System.currentTimeMillis());
        return logoutResponse;
    }

    private LoginResponse doLogin(LoginRequest loginRequest, HttpServletRequest request) {
        //登录返回信息
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setOperator(loginRequest.getOperator());
        loginResponse.setRequestId(loginRequest.getRequestId());

        //验证码校验
        Object cpacha = request.getSession().getAttribute("cpacha");
        //校验session数据是否失效
        if (null == cpacha || !StringUtils.hasLength(String.valueOf(cpacha))) {
            //验证码已失效
            loginResponse.setResponseCode(ResponseCodeEnum.CPACHA_INVALID);
            loginResponse.setTimestamp(System.currentTimeMillis());
            return loginResponse;
        } else {
            String[] cpachaCode = cpacha.toString().split("_");
            long time = Long.parseLong(cpachaCode[1]);
            long now = System.currentTimeMillis();
            if (now - time > cpachaTimeout) {
                //验证码已过期
                loginResponse.setResponseCode(ResponseCodeEnum.CPACHA_INVALID);
                loginResponse.setTimestamp(System.currentTimeMillis());
                return loginResponse;
            }
            // 设置不区分大小写
            if (!loginRequest.getCaptcha().toUpperCase().equals(cpachaCode[0].toUpperCase())) {
                //验证码输入错误
                loginResponse.setResponseCode(ResponseCodeEnum.CPACHA_INVALID);
                loginResponse.setTimestamp(System.currentTimeMillis());
                return loginResponse;
            }
        }


        //登录信息校验
        if ("1".equals(loginRequest.getType())) {


            // 超级管理员用户
            AdminEntity admingEntity = adminRepository.findByName(loginRequest.getAccount());
            if (null == admingEntity) {
                //账户不存在
                loginResponse.setResponseCode(ResponseCodeEnum.ACCOUNT_NOT_EXIST);
                loginResponse.setTimestamp(System.currentTimeMillis());
                return loginResponse;
            } else {
                if (!loginRequest.getPassword().equals(admingEntity.getPassword())) {
                    //密码错误
                    loginResponse.setResponseCode(ResponseCodeEnum.PASSWORD_ERROR);
                    loginResponse.setTimestamp(System.currentTimeMillis());
                    return loginResponse;
                } else {
                    request.getSession().setAttribute("user", loginRequest.getAccount());
                    request.getSession().setAttribute("userType", loginRequest.getType());
                    loginResponse.setResponseCode(ResponseCodeEnum.SUCCESS);
                    loginResponse.setTimestamp(System.currentTimeMillis());
                    return loginResponse;
                }
            }
        } else if ("3".equals(loginRequest.getType())) {


            // 学生登录
            StudentEntity studentEntity = studentRepository.findBySn(loginRequest.getAccount());
            if (null == studentEntity) {
                List<StudentEntity> studentEntityList = studentRepository.findByName(loginRequest.getAccount());
                if (studentEntityList.size() > 1) {
                    loginResponse.setResponseCode(ResponseCodeEnum.SAME_NAME);
                    loginResponse.setTimestamp(System.currentTimeMillis());
                    return loginResponse;
                } else if (studentEntityList.size() == 1) {
                    studentEntity = studentEntityList.get(0);
                } else {
                    loginResponse.setResponseCode(ResponseCodeEnum.ACCOUNT_NOT_EXIST);
                    loginResponse.setTimestamp(System.currentTimeMillis());
                    return loginResponse;
                }
            }
            if (!loginRequest.getPassword().equals(studentEntity.getPassword())) {
                //密码错误
                loginResponse.setResponseCode(ResponseCodeEnum.PASSWORD_ERROR);
                loginResponse.setTimestamp(System.currentTimeMillis());
                return loginResponse;
            } else {
                request.getSession().setAttribute("user", loginRequest.getAccount());
                request.getSession().setAttribute("userType", loginRequest.getType());
                loginResponse.setResponseCode(ResponseCodeEnum.SUCCESS);
                loginResponse.setTimestamp(System.currentTimeMillis());
                return loginResponse;
            }
        } else if ("2".equals(loginRequest.getType())) {


            // 宿管登录
            DormitoryManagerEntity dormitoryManagerEntity = dormitoryManagerRepository.findBySn(loginRequest.getAccount());
            if (null == dormitoryManagerEntity) {
                List<DormitoryManagerEntity> dormitoryManagerEntityList = dormitoryManagerRepository.findByName(loginRequest.getAccount());
                if (dormitoryManagerEntityList.size() > 1) {
                    loginResponse.setResponseCode(ResponseCodeEnum.SAME_NAME);
                    loginResponse.setTimestamp(System.currentTimeMillis());
                    return loginResponse;
                } else if (dormitoryManagerEntityList.size() == 1) {
                    dormitoryManagerEntity = dormitoryManagerEntityList.get(0);
                } else {
                    loginResponse.setResponseCode(ResponseCodeEnum.ACCOUNT_NOT_EXIST);
                    loginResponse.setTimestamp(System.currentTimeMillis());
                    return loginResponse;
                }
            }
            if (!loginRequest.getPassword().equals(dormitoryManagerEntity.getPassword())) {
                //密码错误
                loginResponse.setResponseCode(ResponseCodeEnum.PASSWORD_ERROR);
                loginResponse.setTimestamp(System.currentTimeMillis());
                return loginResponse;
            } else {
                request.getSession().setAttribute("user", loginRequest.getAccount());
                request.getSession().setAttribute("userType", loginRequest.getType());
                loginResponse.setResponseCode(ResponseCodeEnum.SUCCESS);
                loginResponse.setTimestamp(System.currentTimeMillis());
                return loginResponse;
            }
        } else {


            //不支持其他登录方式
            loginResponse.setResponseCode(ResponseCodeEnum.PARAM_ERROR);
            loginResponse.setTimestamp(System.currentTimeMillis());
            return loginResponse;
        }
    }
}
