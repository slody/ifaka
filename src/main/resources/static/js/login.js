new Vue({
    el: "#app",
    data() {
        return {
            username: "",
            password: ""
        }
    },
    methods: {
        login() {
            if (this.username === "" || this.password === "") {
                Swal.fire({
                    icon: 'warning',
                    title: '登录提示',
                    text: "请填写所有内容",
                    showConfirmButton: false,
                    timer: 1500
                })
            }
            axios({
                url: "/:/login.do",
                method: "post",
                data: {
                    username: this.username,
                    password: this.password
                }
            }).then(response => {
                    if (response.data.status === "true") {
                        Swal.fire({
                            icon: "success",
                            title: "登录成功",
                            text: "即将跳转至后台页面",
                            showConfirmButton: false,
                            timer: 1500
                        });
                        setTimeout(function () {
                            window.location.href = "/:/"
                        }, 1500)
                    } else if (response.data.status === "false") {
                        Swal.fire({
                            icon: "error",
                            title: "登陆失败",
                            text: "账号或密码错误",
                            showConfirmButton: false,
                            timer: 1500
                        });
                    } else if (response.data.status === "block") {
                        Swal.fire({
                            icon: "warning",
                            title: "登录失败",
                            text:"账号已被锁定",
                            showConfirmButton: false,
                            timer: 1500
                        });
                    } else {
                        Swal.fire({
                            icon: 'error',
                            title: '登录失败',
                            text: "后台发生错误",
                            showConfirmButton: false,
                            timer: 1500
                        });
                    }
                }
            ).catch(() => {
                Swal.fire({
                    icon: 'error',
                    title: '登录失败',
                    text: "后台发生错误",
                    showConfirmButton: false,
                    timer: 1500
                })
            })
        }
    }
});