package com.lry.servlet.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONArray;
import com.lry.pojo.Role;
import com.lry.pojo.User;
import com.lry.service.role.RoleService;
import com.lry.service.role.RoleServiceImpl;
import com.lry.service.user.UserService;
import com.lry.service.user.UserServiceImpl;
import com.lry.util.PageSupport;

import static com.lry.util.Constants.USER_SESSION;

/**
 * 实现Servlet复用
 *
 * @author 刘汝杨
 */
public class UserServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if (method != null && method.equals("savepwd")) {
            this.updatePwd(req, resp);
        } else if (method != null && method.equals("pwdmodify")) {
            this.pwdModify(req, resp);
        } else if (method != null && method.equals("query")) {
            this.query(req, resp);
        } else if (method != null && method.equals("getrolelist")) {
            this.queryRoles(req, resp);
        } else if (method != null && method.equals("ucexist")) {
            this.userExist(req, resp);
        } else if (method != null && method.equals("add")) {
            this.addUser(req, resp);
        } else if (method != null && method.equals("modify")) {
            this.getUserById(req, resp);
        } else if (method != null && method.equals("modifyexe")) {
            this.modifyUser(req, resp);
        } else if (method != null && method.equals("deluser")) {
            this.delUser(req, resp);
        } else if (method != null && method.equals("view")) {
            this.viewUser(req, resp);
        }
    }

    //查看单个用户信息
    public void viewUser(HttpServletRequest req, HttpServletResponse resp) {
        String userId = req.getParameter("uid");
        System.out.println(userId);

        UserService userService = new UserServiceImpl();

        User user = userService.getUserById(userId);
        System.out.println("查询到的用户信息：");
        System.out.println(user);
        req.setAttribute("user", user);
        try {
            req.getRequestDispatcher("userview.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //删除用户
    public void delUser(HttpServletRequest req, HttpServletResponse resp) {
        int affectRows = 0;
        String uid = req.getParameter("uid");

        UserService userService = new UserServiceImpl();

        HashMap<String, String> map = new HashMap<>();

        try {
            if (userService.getUserById(uid) == null) {
                //该用户不存在
                map.put("delResult", "notexist");
            } else if (!(userService.delUser(Integer.parseInt(uid)) > 0)) {
                //删除失败
                map.put("delResult", "false");
            } else {
                //删除成功
                map.put("delResult", "true");
            }
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(map));

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //修改用户
    public void modifyUser(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute(USER_SESSION);
        int operaterId = user.getId();

        String uid = req.getParameter("uid");
        String userName = req.getParameter("userName");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");

        int affectRows = 0;

        UserService userService = new UserServiceImpl();
        affectRows = userService.modifyUser(uid, userName, gender, birthday, phone, address, userRole, operaterId);

        if (affectRows > 0) {
            try {
                resp.sendRedirect("/jsp/user.do?method=query");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                req.getRequestDispatcher("usermodify.jsp").forward(req, resp);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //修改用户信息时，根据返回的用户ID查询用户的信息
    public void getUserById(HttpServletRequest req, HttpServletResponse resp) {
        String userId = req.getParameter("uid");
        System.out.println(userId);

        UserService userService = new UserServiceImpl();

        User user = userService.getUserById(userId);
        System.out.println("查询到的用户信息：");
        System.out.println(user);
        req.setAttribute("user", user);
        try {
            req.getRequestDispatcher("usermodify.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //添加用户
    public void addUser(HttpServletRequest req, HttpServletResponse resp) {
        String userCode = req.getParameter("userCode");
        String userName = req.getParameter("userName");
        String userPassword = req.getParameter("userPassword");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");

        //该用户的创建者的信息
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute(USER_SESSION);
        int createrId = user.getId();

        int succeeded = 0;
        UserService userService = new UserServiceImpl();
        succeeded = userService.addUser(userCode, userName, userPassword, gender, birthday, phone, address, userRole, createrId);


        if (succeeded != 0) {
            try {
                resp.sendRedirect("/jsp/user.do?method=query");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                req.getRequestDispatcher("useradd.jsp").forward(req, resp);
            } catch (ServletException | IOException e) {
                e.printStackTrace();
            }
        }

    }

    //获取Role列表
    public void queryRoles(HttpServletRequest req, HttpServletResponse resp) {
        //获取角色列表展示
        RoleService roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();

//        万能的Map集合
//        Map<Integer, String> resultMap = new HashMap<>();

//        List<Object> list = new ArrayList<>();
//        //将查询到的roles列表添加到Map集合中
//        for (Role role : roleList) {
//            resultMap.put(role.getId(), role.getRoleName());
//            list.add(role.getRoleName());
//            System.out.println(role.getRoleName());
//        }

        resp.setContentType("application/json");

        try {
            PrintWriter writer = resp.getWriter();
            //JSONArray阿里巴巴的JSON工具
//            writer.write(JSONArray.toJSONString(resultMap));
            writer.write(JSONArray.toJSONString(roleList));

            writer.flush(); //冲洗缓冲流
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //userExist方法：查看用户名是否已经存在
    public void userExist(HttpServletRequest req, HttpServletResponse resp) {
        //获取请求中数据
        String userCode = req.getParameter("userCode");

        Map resultMap = new HashMap<>();

        if (userCode != null && userCode.length() > 0) {
            UserService userService = new UserServiceImpl();
            boolean exist = userService.userCodeExist(userCode);
            System.out.println(exist);
            //如果存在
            if (exist) {
                resultMap.put("userCode", "exist");
            } else {
                resultMap.put("userCode", "noExist");
            }
        } else {
            resultMap.put("userCode", "exist");
        }
        try {
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            //JSONArray阿里巴巴的JSON工具
            writer.write(JSONArray.toJSONString(resultMap));

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //更改密码
    public void updatePwd(HttpServletRequest req, HttpServletResponse resp) {
        Object o = req.getSession().getAttribute(USER_SESSION);

        String password = req.getParameter("newpassword");

        boolean flag = false;
        if (o != null && password.length() > 0) {
            UserService userService = new UserServiceImpl();

            //调用service层
            flag = userService.updatePwd(((User) o).getId(), password);
            System.out.println(flag);
            if (flag) {
                req.setAttribute("message", "密码修改成功，请退出，使用新密码重新登录");
                //面膜修改失败，移除当前的session中的用户信息
                req.getSession().removeAttribute(USER_SESSION);
            } else {
                req.setAttribute("message", "密码修改失败");
            }
        } else {
            req.setAttribute("message", "新密码设置有问题");
        }

        //密码修改成功、失败，都要跳转界面到pwdmodify.jsp
        try {
            req.getRequestDispatcher("pwdmodify.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //验证旧密码
    public void pwdModify(HttpServletRequest req, HttpServletResponse resp) {
        Object o = req.getSession().getAttribute(USER_SESSION);

        String oldpassword = req.getParameter("oldpassword");

        //万能的Map集合
        Map<String, String> resultMap = new HashMap<>();

        //Session失效了，Session过期了
        if (o == null) {
            resultMap.put("result", "sessionerror");
        } else if (oldpassword == null || oldpassword.length() == 0) {//输入的密码为空
            resultMap.put("result", "error");
        } else {
            String userpassword = ((User) o).getUserPassword();//session中用户的密码
            if (oldpassword.equals(userpassword)) {
                resultMap.put("result", "true");
            } else {
                resultMap.put("result", "false");
            }
        }

        try {
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            //JSONArray阿里巴巴的JSON工具
            writer.write(JSONArray.toJSONString(resultMap));

            writer.flush(); //冲洗缓冲流
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //query方法：查询用户列表
    public void query(HttpServletRequest req, HttpServletResponse resp) {
        //查询用户列表

        //从前端获取数据（全是String类型）
        String queryname = req.getParameter("queryname");
        String temp = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");
        System.out.println("pageindex------>" + pageIndex);

        //用于获取用户列表
        UserService userService = new UserServiceImpl();
        List<User> userList = null;

        //获取到的数据转换下格式，用于服务器处理用
        int queryUserRole = 0;
        //第一次走这个请求，一定是第一页，页面大小固定的
        //可以把这些放到配置文件中，方便后期修改
        int currentPageNo = 1;//默认是第一页
        int pageSize = 6;     //默认每页有5条数据


        if (queryname == null) {
            queryname = "";
        }
        //如果没有传入角色信息，就默认角色是0，若有角色信息，将String类型的角色信息转换为int类型
        if (temp != null && !temp.equals("")) {
            queryUserRole = Integer.parseInt(temp);
        }
        if (pageIndex != null && !pageIndex.equals("")) {
            currentPageNo = Integer.parseInt(pageIndex);
        }
        System.out.println("currentPageNo------>" + currentPageNo);

        //获取用户的总数（分页：上一页，下一页的情况）
        int totalUserCount = userService.getUserCount(queryname, queryUserRole);
        System.out.println("totalUserCount------>" + totalUserCount);

        //总页数支持
        int totalPages = new PageSupport().maxPagesNums(totalUserCount, pageSize);
        System.out.println("totalPages------>" + totalPages);

        //控制首页和伟业
        //如果页面要小于1了，就显示第一页的东西
        if (currentPageNo < 1) {
            currentPageNo = 1;
        } else if (currentPageNo > totalPages) {
            currentPageNo = totalPages;
        }
        System.out.println("currentPageNo------>" + currentPageNo);

        //获取用户列表展示
        userList = userService.getUserList(queryname, queryUserRole, currentPageNo, pageSize);

        //获取角色列表展示
        RoleService roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();

        //返回数据给前端
        req.setAttribute("userList", userList);
        req.setAttribute("roleList", roleList);
        req.setAttribute("queryUserName", queryname);
        req.setAttribute("queryUserRole", queryUserRole);
        req.setAttribute("totalPageCount", totalPages);
        req.setAttribute("totalCount", totalUserCount);
        req.setAttribute("currentPageNo", currentPageNo);

        //返回前端
        try {
            req.getRequestDispatcher("userlist.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
}
