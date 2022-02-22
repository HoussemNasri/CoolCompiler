class Test{
    m():Object{
         case var of
	        a : A => out_string("Class type is now A\n");
	        b : B => out_string("Class type is now B\n");
	        c : C => out_string("Class type is now C\n");
	        d : D => out_string("Class type is now D\n");
	        e : E => out_string("Class type is now E\n");
	        o : Object => out_string("Oooops\n");
        esac
    };
};