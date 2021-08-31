package miyagi

class UtilService {
    def normalizeInputs(name, startDate, endDate) {
        if (!name?.trim()) {
            name = "%"
        }
        if (!startDate?.trim()) {
            Calendar cal = Calendar.getInstance()
            cal.set(1, 1, 1);
            startDate = cal.getTime();
        } else {
            startDate = Date.parse("yyyy-MM-dd", startDate)
        }
        if (!endDate?.trim()) {
            Calendar cal = Calendar.getInstance()
            cal.set(3000, 1, 1);
            endDate = cal.getTime();
        } else {
            endDate = Date.parse("yyyy-MM-dd", endDate)
        }

        return[name, startDate, endDate]
    }
}
