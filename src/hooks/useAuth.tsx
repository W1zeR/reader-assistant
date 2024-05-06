import { signOut, useSession } from "next-auth/react";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

export default function useAuth(shouldRedirect: boolean) {
  const { data: session } = useSession();
  const router = useRouter();
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    if (session?.error === "RefreshAccessTokenError") {
      signOut({ callbackUrl: "/signin", redirect: shouldRedirect });
    }

    if (session === null) {
      router.replace("/signin");
      setIsAuthenticated(false);
    } else if (session !== undefined) {
      setIsAuthenticated(true);
    }
  }, [router, session, shouldRedirect]);

  return isAuthenticated;
}
