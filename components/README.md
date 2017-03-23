This sub-project implements the logical application (the interactor) and the components.

Perhaps each component would have its own sub-project, but this would create many small jars and  would get unmanageable.  To keep components isolated and interacting only through their definitions, only Provider classes are allowed to be public here.