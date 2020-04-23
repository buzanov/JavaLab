<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Login</title>
</head>
<body>
    <form method="post" action="/login">
        <div>
            <input type="text" name="login" placeholder="Enter your login" required/>
        </div>
        <div>
            <input type="password" name="password" placeholder="Enter your password" required/>
        </div>
        <div>
            <input type="submit" value="Login"/>
        </div>
    </form>
</body>
</html>