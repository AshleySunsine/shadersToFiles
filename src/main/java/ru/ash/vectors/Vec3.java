package ru.ash.vectors;

public class Vec3 {
    public float x, y, z;

    // Конструкторы
    public Vec3() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
    }

    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3(float value) {
        this.x = value;
        this.y = value;
        this.z = value;
    }

    // Копирующий конструктор
    public Vec3(Vec3 other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    // Базовые арифметические операции

    // Сложение
    public Vec3 add(Vec3 other) {
        return new Vec3(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    public Vec3 add(float value) {
        return new Vec3(this.x + value, this.y + value, this.z + value);
    }

    // Вычитание
    public Vec3 subtract(Vec3 other) {
        return new Vec3(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    public Vec3 subtract(float value) {
        return new Vec3(this.x - value, this.y - value, this.z - value);
    }

    // Умножение
    public Vec3 multiply(Vec3 other) {
        return new Vec3(this.x * other.x, this.y * other.y, this.z * other.z);
    }

    public Vec3 multiply(float scalar) {
        return new Vec3(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    // Деление
    public Vec3 divide(Vec3 other) {
        return new Vec3(this.x / other.x, this.y / other.y, this.z / other.z);
    }

    public Vec3 divide(float scalar) {
        return new Vec3(this.x / scalar, this.y / scalar, this.z / scalar);
    }

    // Скалярное произведение (dot product)
    public float dot(Vec3 other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    // Векторное произведение (cross product) - на всякий случай
    public Vec3 cross(Vec3 other) {
        return new Vec3(
                this.y * other.z - this.z * other.y,
                this.z * other.x - this.x * other.z,
                this.x * other.y - this.y * other.x
        );
    }

    // Длина вектора
    public float length() {
        return (float)Math.sqrt(x * x + y * y + z * z);
    }

    // Квадрат длины (более быстрая операция)
    public float lengthSquared() {
        return x * x + y * y + z * z;
    }

    // Нормализация (возвращает новый нормализованный вектор)
    public Vec3 normalize() {
        float len = length();
        if (len == 0.0f) {
            return new Vec3(0, 0, 0);
        }
        return divide(len);
    }

    // Нормализация на месте (изменяет текущий вектор)
    public Vec3 normalizeSelf() {
        float len = length();
        if (len != 0.0f) {
            x /= len;
            y /= len;
            z /= len;
        }
        return this;
    }

    // Расстояние до другого вектора
    public float distanceTo(Vec3 other) {
        float dx = x - other.x;
        float dy = y - other.y;
        float dz = z - other.z;
        return (float)Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    // Применение математических функций поэлементно
    public Vec3 sin() {
        return new Vec3((float)Math.sin(x), (float)Math.sin(y), (float)Math.sin(z));
    }

    public Vec3 cos() {
        return new Vec3((float)Math.cos(x), (float)Math.cos(y), (float)Math.cos(z));
    }

    // Абсолютное значение
    public Vec3 abs() {
        return new Vec3(Math.abs(x), Math.abs(y), Math.abs(z));
    }

    // Ограничение значений (clamp)
    public Vec3 clamp(float minVal, float maxVal) {
        return new Vec3(
                Math.max(minVal, Math.min(maxVal, x)),
                Math.max(minVal, Math.min(maxVal, y)),
                Math.max(minVal, Math.min(maxVal, z))
        );
    }

    // Ограничение значений (clamp) с разными границами для компонент
    public Vec3 clamp(Vec3 minVec, Vec3 maxVec) {
        return new Vec3(
                Math.max(minVec.x, Math.min(maxVec.x, x)),
                Math.max(minVec.y, Math.min(maxVec.y, y)),
                Math.max(minVec.z, Math.min(maxVec.z, z))
        );
    }

    // Получение максимальной/минимальной компоненты
    public float maxComponent() {
        return Math.max(x, Math.max(y, z));
    }

    public float minComponent() {
        return Math.min(x, Math.min(y, z));
    }

    // Сравнение с нулём (почти ноль)
    public boolean isZero(float epsilon) {
        return Math.abs(x) < epsilon && Math.abs(y) < epsilon && Math.abs(z) < epsilon;
    }

    // Перестановка компонентов (для .yzx из GLSL)
    public Vec3 yzx() {
        return new Vec3(y, z, x);
    }

    public Vec3 zxy() {
        return new Vec3(z, x, y);
    }

    // Статические методы для удобства

    public static Vec3 add(Vec3 a, Vec3 b) {
        return new Vec3(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    public static Vec3 subtract(Vec3 a, Vec3 b) {
        return new Vec3(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    public static Vec3 multiply(Vec3 a, float scalar) {
        return new Vec3(a.x * scalar, a.y * scalar, a.z * scalar);
    }

    public static float dot(Vec3 a, Vec3 b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    // Создание стандартных векторов
    public static Vec3 zero() {
        return new Vec3(0, 0, 0);
    }

    public static Vec3 one() {
        return new Vec3(1, 1, 1);
    }

    public static Vec3 unitX() {
        return new Vec3(1, 0, 0);
    }

    public static Vec3 unitY() {
        return new Vec3(1, 0, 0);
    }

    public static Vec3 unitZ() {
        return new Vec3(0, 0, 1);
    }

    @Override
    public String toString() {
        return String.format("(%.3f, %.3f, %.3f)", x, y, z);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vec3 vec3 = (Vec3) obj;
        return Float.compare(vec3.x, x) == 0 &&
                Float.compare(vec3.y, y) == 0 &&
                Float.compare(vec3.z, z) == 0;
    }

    @Override
    public int hashCode() {
        int result = Float.floatToIntBits(x);
        result = 31 * result + Float.floatToIntBits(y);
        result = 31 * result + Float.floatToIntBits(z);
        return result;
    }
}
