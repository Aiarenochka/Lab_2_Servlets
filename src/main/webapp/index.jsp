<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Практична робота №2. Сервлети</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<div class="container">
    <h1>Практична робота №2. Сервлети</h1>
    <p>Варіант 10</p>

    <div class="card">
        <h2>Завдання 2.1. Ряд 1/(1+x)^2 = 1 - 2x + 3x^2 - 4x^3 + ... (R = 1)</h2>
        <form action="series-servlet" method="post">
            <label>x, |x| &lt; 1:
                <input type="text" name="x" value="0.5">
            </label>
            <label>n (кількість доданків):
                <input type="text" name="n" value="10">
            </label>
            <label>e (точність, e &gt; 0):
                <input type="text" name="e" value="0.001">
            </label>
            <button type="submit">Обчислити</button>
        </form>
    </div>

    <div class="card">
        <h2>Завдання 2.2. Табулювання функції y = x^0.3</h2>
        <form action="tabulate-servlet" method="get">
            <label>a (початок відрізка):
                <input type="text" name="a" value="0">
            </label>
            <label>b (кінець відрізка):
                <input type="text" name="b" value="5">
            </label>
            <label>h (крок, h &gt; 0):
                <input type="text" name="h" value="0.5">
            </label>
            <button type="submit">Табулювати</button>
        </form>
    </div>
</div>
</body>
</html>