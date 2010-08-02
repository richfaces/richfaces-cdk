        if (attributeValue == null) {
            return false;
        } else if (attributeValue instanceof String) {
            return ((String) attributeValue).length()>0;
        } else if (attributeValue instanceof Boolean && Boolean.FALSE.equals(attributeValue)) {
            return false;
        } else if (attributeValue instanceof Integer && (Integer) attributeValue == Integer.MIN_VALUE) {
            return false;
        } else if (attributeValue instanceof Double && (Double) attributeValue == Double.MIN_VALUE) {
            return false;
        } else if (attributeValue instanceof Character && (Character) attributeValue == Character.MIN_VALUE) {
            return false;
        } else if (attributeValue instanceof Float && (Float) attributeValue == Float.MIN_VALUE) {
            return false;
        } else if (attributeValue instanceof Short && (Short) attributeValue == Short.MIN_VALUE) {
            return false;
        } else if (attributeValue instanceof Byte && (Byte) attributeValue == Byte.MIN_VALUE) {
            return false;
        } else if (attributeValue instanceof Long && (Long) attributeValue == Long.MIN_VALUE) {
            return false;
        }

        return attributeValue.toString().length()>0;
