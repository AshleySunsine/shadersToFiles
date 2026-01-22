package ru.ash.matrix;

import ru.ash.vectors.Vec2;

public class Mat2 {
    public float m00, m01, m10, m11;

    // Конструкторы

    // Конструктор по умолчанию (единичная матрица)
    public Mat2() {
        // Единичная матрица
        this.m00 = 1.0f;
        this.m01 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = 1.0f;
    }

    // Конструктор с явным заданием всех элементов
    public Mat2(float m00, float m01, float m10, float m11) {
        this.m00 = m00;
        this.m01 = m01;
        this.m10 = m10;
        this.m11 = m11;
    }

    // Конструктор из массива (по строкам)
    public Mat2(float[] values) {
        if (values.length >= 4) {
            this.m00 = values[0];
            this.m01 = values[1];
            this.m10 = values[2];
            this.m11 = values[3];
        } else {
            // Единичная матрица по умолчанию
            this.m00 = 1.0f;
            this.m01 = 0.0f;
            this.m10 = 0.0f;
            this.m11 = 1.0f;
        }
    }

    // Конструктор копирования
    public Mat2(Mat2 other) {
        this.m00 = other.m00;
        this.m01 = other.m01;
        this.m10 = other.m10;
        this.m11 = other.m11;
    }

    // Конструктор матрицы вращения
    public static Mat2 rotation(float angle) {
        float cosA = (float)Math.cos(angle);
        float sinA = (float)Math.sin(angle);
        return new Mat2(
                cosA, -sinA,
                sinA,  cosA
        );
    }

    // Конструктор матрицы масштабирования
    public static Mat2 scaling(float scaleX, float scaleY) {
        return new Mat2(
                scaleX, 0.0f,
                0.0f,   scaleY
        );
    }

    public static Mat2 scaling(float scale) {
        return scaling(scale, scale);
    }

    // Основные операции

    // Умножение матрицы на вектор
    public Vec2 multiply(Vec2 v) {
        return new Vec2(
                m00 * v.x + m01 * v.y,
                m10 * v.x + m11 * v.y
        );
    }

    // Умножение матрицы на матрицу
    public Mat2 multiply(Mat2 other) {
        return new Mat2(
                m00 * other.m00 + m01 * other.m10,  // (0,0)
                m00 * other.m01 + m01 * other.m11,  // (0,1)
                m10 * other.m00 + m11 * other.m10,  // (1,0)
                m10 * other.m01 + m11 * other.m11   // (1,1)
        );
    }

    // Сложение матриц
    public Mat2 add(Mat2 other) {
        return new Mat2(
                m00 + other.m00,
                m01 + other.m01,
                m10 + other.m10,
                m11 + other.m11
        );
    }

    // Вычитание матриц
    public Mat2 subtract(Mat2 other) {
        return new Mat2(
                m00 - other.m00,
                m01 - other.m01,
                m10 - other.m10,
                m11 - other.m11
        );
    }

    // Умножение на скаляр
    public Mat2 multiply(float scalar) {
        return new Mat2(
                m00 * scalar,
                m01 * scalar,
                m10 * scalar,
                m11 * scalar
        );
    }

    // Транспонирование матрицы
    public Mat2 transpose() {
        return new Mat2(
                m00, m10,  // меняем местами m01 и m10
                m01, m11
        );
    }

    // Определитель матрицы
    public float determinant() {
        return m00 * m11 - m01 * m10;
    }

    // Обратная матрица (если определитель не равен 0)
    public Mat2 inverse() {
        float det = determinant();
        if (Math.abs(det) < 1e-8f) {
            // Матрица вырождена, возвращаем нулевую матрицу
            return new Mat2(0, 0, 0, 0);
        }
        float invDet = 1.0f / det;
        return new Mat2(
                m11 * invDet, -m01 * invDet,
                -m10 * invDet,  m00 * invDet
        );
    }

    // Получение элемента по индексам
    public float get(int row, int col) {
        if (row == 0) {
            return (col == 0) ? m00 : m01;
        } else {
            return (col == 0) ? m10 : m11;
        }
    }

    // Установка элемента по индексам
    public void set(int row, int col, float value) {
        if (row == 0) {
            if (col == 0) m00 = value;
            else m01 = value;
        } else {
            if (col == 0) m10 = value;
            else m11 = value;
        }
    }

    // Преобразование в массив
    public float[] toArray() {
        return new float[] {m00, m01, m10, m11};
    }

    // Преобразование в массив по строкам (для OpenGL/GLSL совместимости)
    public float[] toArrayRowMajor() {
        return new float[] {m00, m01, m10, m11};
    }

    // Преобразование в массив по столбцам (альтернативный формат)
    public float[] toArrayColumnMajor() {
        return new float[] {m00, m10, m01, m11};
    }

    // Проверка на единичную матрицу
    public boolean isIdentity() {
        return Math.abs(m00 - 1.0f) < 1e-6f &&
                Math.abs(m01) < 1e-6f &&
                Math.abs(m10) < 1e-6f &&
                Math.abs(m11 - 1.0f) < 1e-6f;
    }

    // Проверка на нулевую матрицу
    public boolean isZero() {
        return Math.abs(m00) < 1e-6f &&
                Math.abs(m01) < 1e-6f &&
                Math.abs(m10) < 1e-6f &&
                Math.abs(m11) < 1e-6f;
    }

    // Создание матрицы из угла (как в GLSL: mat2(cos, -sin, sin, cos))
    public static Mat2 fromAngle(float angle) {
        return rotation(angle);
    }

    // Для GLSL: mat2(cos(t/4.+vec4(0,33,11,0))) - специальный случай
    // В GLSL это означает: mat2(cos(a), -sin(a), sin(a), cos(a)) где a = t/4.0
    public static Mat2 fromGLSLRotation(float time) {
        float angle = time / 4.0f;
        return rotation(angle);
    }

    // Статические константы

    public static Mat2 identity() {
        return new Mat2(1, 0, 0, 1);
    }

    public static Mat2 zero() {
        return new Mat2(0, 0, 0, 0);
    }

    @Override
    public String toString() {
        return String.format("[%.3f, %.3f]\n[%.3f, %.3f]",
                m00, m01, m10, m11);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Mat2 mat2 = (Mat2) obj;
        return Float.compare(mat2.m00, m00) == 0 &&
                Float.compare(mat2.m01, m01) == 0 &&
                Float.compare(mat2.m10, m10) == 0 &&
                Float.compare(mat2.m11, m11) == 0;
    }

    @Override
    public int hashCode() {
        int result = Float.floatToIntBits(m00);
        result = 31 * result + Float.floatToIntBits(m01);
        result = 31 * result + Float.floatToIntBits(m10);
        result = 31 * result + Float.floatToIntBits(m11);
        return result;
    }
}
