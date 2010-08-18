if (object == null) {
    return false;
}

if (object instanceof Boolean) {
    return Boolean.TRUE.equals(object);
}

return Boolean.valueOf(object.toString());
