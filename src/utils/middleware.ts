import { getToken } from "next-auth/jwt";
import { withAuth } from "next-auth/middleware";
import { NextResponse } from "next/server";

export default async function middleware(req, event) {
  const token = await getToken({ req });
  const isAuthenticated = !!token;

  // If user is already authenticated
  if ((req.nextUrl.pathname.startsWith("/signin") || req.nextUrl.pathname.startsWith("/signup")) && isAuthenticated) {
    return NextResponse.redirect(new URL("/", req.url));
  }

  // If user isn't authenticated
  if ((req.nextUrl.pathname.startsWith("/private") ||
    req.nextUrl.pathname.startsWith("/pending") ||
    req.nextUrl.pathname.startsWith("/settings")) && !isAuthenticated) {
    return NextResponse.redirect(new URL("/signin", req.url));
  }

  const authMiddleware = withAuth({
    pages: {
      signIn: `/signin`
    }
  });

  return authMiddleware(req, event);
}

// Config to match all pages
export const config = {
  matcher: ["/((?!api|_next/static|_next/image|.*\\.png$).*)"]
};