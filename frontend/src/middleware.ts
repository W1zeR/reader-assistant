import { getToken } from "next-auth/jwt";
import { withAuth } from "next-auth/middleware";
import { NextResponse } from "next/server";

export default async function middleware(req, event) {
  const API_URL = process.env.NEXT_PUBLIC_API_URL;
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

  if (req.nextUrl.pathname.startsWith("/signup")) {
    return NextResponse.next(req.url);
  }

  if (req.nextUrl.pathname.startsWith("/pending")) {
    const res = await fetch(API_URL + `/profiles/me`, {
        headers: {
          Authorization: `Bearer ${token.accessToken}`
        }
      }
    );

    const profile = await res.json();

    const isStaff = profile.roles.some(r => r.name === "ROLE_MODERATOR" ||
      r.name === "ROLE_ADMIN");

    if (isStaff) {
      return NextResponse.next(req.url);
    } else {
      return NextResponse.redirect(new URL("/", req.url));
    }
  }

  const authMiddleware = withAuth({
    pages: {
      signIn: `/signin`
    }
  });

  return authMiddleware(req, event);
}

export const config = {
  matcher: ["/private", "/pending", "/settings", "/signin", "/signup"]
};