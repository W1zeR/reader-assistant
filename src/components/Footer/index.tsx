"use client";
import Image from "next/image";
import Link from "next/link";

const Footer = () => {
  return (
    <>
      <footer className="relative z-10 bg-white dark:bg-gray-dark mt-auto">
        <div className="container">
          <div className="h-px w-full bg-gradient-to-r from-transparent via-[#D2D8E183] to-transparent dark:via-[#959CB183]"></div>
          <div className="py-4 flex justify-center">
            <Link href="/" className="inline-block mr-10">
              <Image
                src="/images/logo/logo-2.svg"
                alt="logo"
                className="w-full dark:hidden"
                width={140}
                height={30}
              />
              <Image
                src="/images/logo/logo.svg"
                alt="logo"
                className="hidden w-full dark:block"
                width={140}
                height={30}
              />
            </Link>
            <p className="text-center text-base text-body-color dark:text-white">
              Sergey Lavrov 2024
            </p>
          </div>
        </div>
      </footer>
    </>
  );
};

export default Footer;
