<html>  
    <#include "../common/header.ftl"> 
    <body>  
        <div class="container-fluid">
            <div class="row clearfix">
                <div class="col-md-6 column col-md-offset-3">
                    <form role="form" method="post" action="/seller/login">
                        <div class="form-group">
                            <label>用户名</label>
                            <input name="username" type="text" class="form-control"/>
                        </div>
                        <div class="form-group">
                            <label>密码</label>
                            <input name="password" type="text" class="form-control" />
                        </div>
                        <button type="submit" class="btn btn-default">提交</button>
                    </form>
                </div>
            </div>
        </div> 
    </body>  
</html>  