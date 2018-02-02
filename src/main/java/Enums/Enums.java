package Enums;

public class Enums {
    public enum category {
        SELL(0),
        WANTED(1),
        SWAP(2),
        FREE(3),
        RENT(4),
        INFOBOARD(5),
        MAKEANOFFER(6),
        BID(7);
        private final int category;

        category(int i) {
            this.category = i;
        }

        public int getCategory() {
            return category;
        }
    }

    public enum rentCategory {
        PERHOUR(0),
        PERDAY(1),
        PerWeek(2),
        PerMonth(3),
        OTHER(4),
        PERWEEKEND(5);
        private final int rentCategory;

        rentCategory(int i) {
            this.rentCategory = i;
        }

        public int getRentCategory() {
            return rentCategory;
        }
    }

    public enum itemCondition {
        USED(0),
        NEW(1);
        private final int itemCondition;

        itemCondition(int i) {
            this.itemCondition = i;
        }

        public int getItemCondition() {
            return itemCondition;
        }
    }

}
