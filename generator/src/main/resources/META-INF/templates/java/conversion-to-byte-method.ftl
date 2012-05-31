if (object instanceof Byte) {
    return (Byte) object;
}

return Byte.valueOf(object.toString());