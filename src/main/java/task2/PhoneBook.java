package task2;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PhoneBook {

  private final Map<String, PhoneBookEntry> phoneBook = new TreeMap<>();

  class PhoneBookEntry {

    private String singlePhone;
    private List<String> someNumbers;

    PhoneBookEntry(String phoneNumber) {
      this.singlePhone = phoneNumber;
    }

    PhoneBookEntry add(String phoneNumber) {
      if (someNumbers == null) {
        someNumbers = new LinkedList<>();
        someNumbers.add(singlePhone);
        someNumbers.add(phoneNumber);
      } else {
        someNumbers.add(phoneNumber);
      }
      singlePhone = "";
      return this;
    }

    String getPhoneAsString() {

      if (someNumbers == null) {
        return singlePhone;
      } else {
        return someNumbers.toString();
      }
    }

  }

  public boolean add(String surname, String phone) {
    PhoneBookEntry phoneBookEntry = phoneBook.get(surname);
    if (phoneBookEntry == null) {
      phoneBook.put(surname, new PhoneBookEntry(phone));
    } else {
      phoneBookEntry.add(phone);
    }
    return true;
  }

  public String get(String surname) {
    PhoneBookEntry phoneBookEntry = phoneBook.get(surname);
    if (phoneBookEntry == null) {
      return "Для " + surname + " нет записей";
    } else {
      return surname + " " + phoneBookEntry.getPhoneAsString();
    }
  }
}
