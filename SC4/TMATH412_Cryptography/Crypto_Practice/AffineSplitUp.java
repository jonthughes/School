
public class AffineSplitUp {

    public static void main(String[] args) {
        String input = "UVEFBBSXZSSFPJEWGFOFUVEWBFTLCCAKEHOMISBTSONWGFOFUVEUBFTHUVEWPCRTTHHHVUHBUKEKFWNMISRHPATABHHXFLPXDHEWUVEVIONZFGTHIOVXPQCNSFEWUVEKFKALBGONOROYNORVIWNZCCOMTCUMTWDXUVEWPCRHQSNXEKIMIOCEBBGTZCUGHCFYJQEKTHEIQSDLNORMMMTASCUZIHHXECOKXOY";
        for (int i = 0; i < input.length(); i++) {
            if (i % 4 == 0) {
                System.out.print(input.charAt(i));
            }
        }
        System.out.println();
        for (int i = 0; i < input.length(); i++) {
            if (i % 4 == 1) {
                System.out.print(input.charAt(i));
            }
        }
        System.out.println();
        for (int i = 0; i < input.length(); i++) {
            if (i % 4 == 2) {
                System.out.print(input.charAt(i));
            }
        }
        System.out.println();
        for (int i = 0; i < input.length(); i++) {
            if (i % 4 == 3) {
                System.out.print(input.charAt(i));
            }
        }
        System.out.println();
    }

}
