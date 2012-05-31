if (object instanceof Float) {
    return (Float) object;
}

return Float.valueOf(object.toString());