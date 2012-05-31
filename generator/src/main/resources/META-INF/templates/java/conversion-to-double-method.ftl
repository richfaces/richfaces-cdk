if (object instanceof Double) {
    return (Double) object;
}

return Double.valueOf(object.toString());