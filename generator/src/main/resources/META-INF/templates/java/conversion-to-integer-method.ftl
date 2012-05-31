if (object instanceof Integer) {
    return (Integer) object;
}

return Integer.valueOf(object.toString());