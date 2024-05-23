import React from "react";
import {Dropdown, DropdownTrigger, DropdownMenu, DropdownItem, Button} from "@nextui-org/react";
import { usePathname, useRouter, useSearchParams } from "next/navigation";

export default function PageElements() {
  const [selectedKeys, setSelectedKeys] = React.useState(new Set(["3"]));

  const selectedValue = React.useMemo(
    () => Array.from(selectedKeys).join(", ").replaceAll("_", " "),
    [selectedKeys]
  );

  const pathname = usePathname();
  const searchParams = useSearchParams();
  const { replace } = useRouter();

  const createPageURL = (key: number | string) => {
    const params = new URLSearchParams(searchParams);
    params.set('size', key.toString());
    replace(`${pathname}?${params.toString()}`);
  };

  return (
    <Dropdown>
      <DropdownTrigger>
        <Button
          variant="bordered"
          className="rounded-md border border-gray-700 dark:border-gray-300"
        >
          {selectedValue}
        </Button>
      </DropdownTrigger>
      <DropdownMenu
        aria-label="Single selection example"
        variant="flat"
        disallowEmptySelection
        selectionMode="single"
        selectedKeys={selectedKeys}
        onSelectionChange={setSelectedKeys}
        onAction={(key) => createPageURL(key)}
      >
        <DropdownItem key="3">3</DropdownItem>
        <DropdownItem key="9">9</DropdownItem>
        <DropdownItem key="27">27</DropdownItem>
      </DropdownMenu>
    </Dropdown>
  );
}
